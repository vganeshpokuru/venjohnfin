package org.mifosplatform.mpesa.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.mifosplatform.mpesa.domain.Mpesa;
import org.mifosplatform.mpesa.service.MpesaBridgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mpesa")
public class MifosMpesaController {

	private final Logger logger = LoggerFactory
			.getLogger(MifosMpesaController.class);
	private MpesaBridgeService mpesaBridgeService;

	@Value("${mpesausername}")
	private String mpesausername;

	@Value("${mpesapassword}")
	private String mpesapassword;

	@Autowired
	public MifosMpesaController(final MpesaBridgeService mpesaBridgeService) {
		super();
		this.mpesaBridgeService = mpesaBridgeService;

	}

	@RequestMapping(value = "/incomingmpesa", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> storeTransactionDetails(
			@QueryParam("id") final String id,
			@QueryParam("orig") final String orig,
			@QueryParam("dest") final String dest,
			@QueryParam("tstamp") final String tstamp,
			@QueryParam("text") final String text,
			@QueryParam("user") final String user,
			@QueryParam("pass") final String pass,
			@QueryParam("mpesa_code") final String mpesa_code,
			@QueryParam("mpesa_acc") final String mpesa_acc,
			@QueryParam("mpesa_msisdn") final String mpesa_msisdn,
			@QueryParam("mpesa_trx_date") final String mpesa_trx_date,
			@QueryParam("mpesa_trx_time") final String mpesa_trx_time,
			@QueryParam("mpesa_amt") final BigDecimal mpesa_amt,
			@QueryParam("mpesa_sender") final String mpesa_sender) {
		System.out.println("mpesa_code: " + mpesa_code);
		String responseMessage = "";
		StringBuilder requestMsg = new StringBuilder();

		requestMsg.append(
				"transaction failed to following requested parameters  : ");
		requestMsg.append("   id : ");
		requestMsg.append(id);
		requestMsg.append(", orig: ");
		requestMsg.append(orig);
		requestMsg.append(", dest :");
		requestMsg.append(dest);
		requestMsg.append(", tstamp: ");
		requestMsg.append(tstamp);
		requestMsg.append(", text :");
		requestMsg.append(text);
		requestMsg.append(", user :");
		requestMsg.append(user);
		requestMsg.append(", Pass :");
		requestMsg.append(pass);
		requestMsg.append(", mpesa_code :");
		requestMsg.append(mpesa_code);
		requestMsg.append(", mpesa_acc :");
		requestMsg.append(mpesa_acc);
		requestMsg.append(", mpesa_msisdn : ");
		requestMsg.append(mpesa_msisdn);
		requestMsg.append(", mpesa_trx_date :");
		requestMsg.append(mpesa_trx_date);
		requestMsg.append(", mpesa_trx_time :");
		requestMsg.append(mpesa_trx_time);
		requestMsg.append(", mpesa_amt :");
		requestMsg.append(mpesa_amt);
		requestMsg.append(", mpesa_sender: ");
		requestMsg.append(mpesa_sender);

		String request = requestMsg.toString();

		try {
			Long officeId = (long) 0;
			responseMessage = this.mpesaBridgeService.storeTransactionDetails(
					id, orig, dest, tstamp, text, user, pass, mpesa_code,
					mpesa_acc, mpesa_msisdn, mpesa_trx_date, mpesa_trx_time,
					mpesa_amt, mpesa_sender, "PaidIn", officeId);
			if (responseMessage.equalsIgnoreCase(mpesa_code)) {
				return new ResponseEntity<String>("CONFLICT:" + responseMessage,
						HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			// logger.error("Exception " + e);

			logger.error(request, "Error is :" + e);
			return new ResponseEntity<String>(e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(responseMessage, HttpStatus.OK);

	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@RequestMapping(value = "/getunmappedtransactions", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Collection<Mpesa>> retriveUnmappedTransactions(
			@QueryParam("officeId") final Long officeId,
			@QueryParam("offset") final Integer offset,
			@QueryParam("limit") final Integer limit) {
		Page<Mpesa> transactionDetails = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		responseHeaders.set("Access-Control-Allow-Methods",
				"GET, POST, DELETE, PUT, OPTIONS");
		// responseHeaders.setOrigin("*");
		try {
			// this.mpesaBridgeService.retriveAllTransactions(officeId);
			transactionDetails = this.mpesaBridgeService
					.retriveUnmappedTransactions(officeId, offset, limit);
		} catch (Exception e) {
			logger.error("Exception " + e);
		}
		HashMap<String, Object> responseData = new HashMap<String, Object>();
		responseData.put("totalFilteredRecords",
				transactionDetails.getTotalElements());
		responseData.put("pageItems", transactionDetails.getContent());
		return new ResponseEntity(responseData, HttpStatus.OK);
	}

	@RequestMapping(value = "/postpayment", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Collection<Mpesa>> completePayment(
			@QueryParam("id") final Long id,
			@QueryParam("officeId") final Long officeId,
			@QueryParam("clientId") final Long clientId) {
		HttpHeaders responseHeaders = new HttpHeaders();
		List<Mpesa> transactionDetails = null;
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		responseHeaders.set("Access-Control-Allow-Methods",
				"GET, POST, DELETE, PUT, OPTIONS");
		// responseHeaders.setOrigin("*");
		try {
			transactionDetails = this.mpesaBridgeService.Payment(id, officeId,
					clientId);
		} catch (Exception e) {
			logger.error("Exception " + e);
		}

		return new ResponseEntity<Collection<Mpesa>>(transactionDetails,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/Search", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Collection<Mpesa>> Search(
			@QueryParam("status") final String status,
			@QueryParam("FromDate") final String FromDate,
			@QueryParam("ToDate") final String ToDate,
			@QueryParam("mobileNo") final String mobileNo,
			@QueryParam("officeId") final Long officeId,
			@QueryParam("offset") final Integer offset,
			@QueryParam("limit") final Integer limit) {
		HttpHeaders responseHeaders = new HttpHeaders();
		Page<Mpesa> transactionDetails = null;
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		responseHeaders.set("Access-Control-Allow-Methods",
				"GET, POST, DELETE, PUT, OPTIONS");
		// responseHeaders.setOrigin("*");
		try {
			Date FromDate1 = null;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if (FromDate != null && FromDate != "") {
				FromDate1 = formatter.parse(FromDate);
			} else {
				Date dt = new Date(0);
				FromDate1 = dt;
			}

			Date ToDate1 = formatter.parse(ToDate);
			transactionDetails = this.mpesaBridgeService.searchMpesaDetail(
					status, mobileNo, FromDate1, ToDate1, officeId, offset,
					limit);
		} catch (Exception e) {
			logger.error("Exception " + e);
		}
		HashMap<String, Object> responseData = new HashMap<String, Object>();
		responseData.put("totalFilteredRecords",
				transactionDetails.getTotalElements());
		responseData.put("pageItems", transactionDetails.getContent());

		return new ResponseEntity(responseData, HttpStatus.OK);
	}

}
