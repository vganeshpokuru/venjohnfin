package org.mifosplatform.mpesa.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.ws.rs.QueryParam;

import org.mifosplatform.mpesa.domain.Mpesa;
import org.mifosplatform.mpesa.service.MpesaBridgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	private final Logger logger = LoggerFactory.getLogger(MifosMpesaController.class);
	private MpesaBridgeService mpesaBridgeService;
	
	
	@Autowired
	public MifosMpesaController(final MpesaBridgeService mpesaBridgeService) {
		super();
		this.mpesaBridgeService = mpesaBridgeService;
		
	}


	@RequestMapping(value = "/incomingtransaction", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> storeTransactionDetails(@QueryParam("id") final Long id,@QueryParam("orig") final String orig,
			@QueryParam("dest") final String dest,@QueryParam("tstamp") final String tstamp,@QueryParam("text") final String text,@QueryParam("user")
			final String user,@QueryParam("pass") final String pass,@QueryParam("mpesa_code") final String mpesa_code, @QueryParam("mpesa_acc")
			final String mpesa_acc,@QueryParam("mpesa_msisdn") final String mpesa_msisdn,@QueryParam("mpesa_trx_date") final Date mpesa_trx_date,@QueryParam("mpesa_trx_time")
			final String mpesa_trx_time,@QueryParam("mpesa_amt") final BigDecimal mpesa_amt,@QueryParam("mpesa_sender") final String mpesa_sender){
		String responseMessage = "";
		try{
			if(user.equalsIgnoreCase("caritas") && pass.equalsIgnoreCase("nairobi")){
				responseMessage = this.mpesaBridgeService.storeTransactionDetails(id,orig,dest,tstamp,text,user,pass,mpesa_code,mpesa_acc,
					mpesa_msisdn,mpesa_trx_date,mpesa_trx_time,mpesa_amt,mpesa_sender);
			}
		}catch(Exception e){
			logger.error("Exception " + e);
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(responseMessage,HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value = "/mpesatransactions", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Collection<String>> retriveAllTransactions(){
		Collection<String> transactionDetails = null;
		try{
			transactionDetails = this.mpesaBridgeService.retriveAllTransactions();
		}catch(Exception e){
			logger.error("Exception " + e);
		}
		return new ResponseEntity<>(transactionDetails,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getunmappedtransactions", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Collection<Mpesa>> retriveUnmappedTransactions(){
		Collection<Mpesa> transactionDetails = null;
		 HttpHeaders responseHeaders = new HttpHeaders();
		 responseHeaders.set("Access-Control-Allow-Origin","*");
		 responseHeaders.set("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
		 //responseHeaders.setOrigin("*");
		try{
			this.mpesaBridgeService.retriveAllTransactions();
			transactionDetails = this.mpesaBridgeService.retriveUnmappedTransactions();
		}catch(Exception e){
			logger.error("Exception " + e);
		}
		
		return new ResponseEntity<>(transactionDetails,HttpStatus.OK);
	}
	
	
}
