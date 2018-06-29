package org.mifosplatform.mpesa.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mifosplatform.mpesa.configuration.ClientHelper;
import org.mifosplatform.mpesa.domain.Mpesa;
import org.mifosplatform.mpesa.repository.MpesaBridgeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class MpesaBridgeServiceImpl implements MpesaBridgeService{
	
	 private final Logger logger = LoggerFactory.getLogger(MpesaBridgeServiceImpl.class);
	
	private final MpesaBridgeRepository mpesaBridgeRepository;
	private final String loginURL = "https://localhost:8443/mifosng-provider/api/v1/authentication?username=data&password=yutuchina";

	@Autowired
	public MpesaBridgeServiceImpl(final MpesaBridgeRepository mpesaBridgeRepository) {
		super();
		this.mpesaBridgeRepository = mpesaBridgeRepository;
	}

	
	@Override
	@Transactional
	public String storeTransactionDetails(final Long id, final String origin, final String dest,final String tStamp, final String text, final String user, 
			final String pass, final String mpesaCode, final String mpesaAccount, final String mobileNo,final Date txnDate, final String txnTime, 
			final BigDecimal mpesaAmount, final String sender) {
		Mpesa mpesa = null;
		Mpesa response = null;
		String responseData = "";
		try{
			if(id != null && mpesaCode != null && !mpesaCode.equalsIgnoreCase("")){
				mpesa = new Mpesa();
				mpesa.setIpnId(id);
				mpesa.setOrigin(origin);
				mpesa.setDestination(dest);
				mpesa.setTimeStamp(tStamp);
				mpesa.setTestMessage(text);
				mpesa.setUser(user);
				mpesa.setPassword(pass);
				mpesa.setTransactionCode(mpesaCode);
				mpesa.setAccountName(mpesaAccount);
				mpesa.setMobileNo(mobileNo);
				mpesa.setTransactionDate(txnDate);
				mpesa.setTransactionTime(txnTime);
				mpesa.setTransactionAmount(mpesaAmount);
				mpesa.setSender(sender);
				mpesa.setStatus("R");
				response = this.mpesaBridgeRepository.save(mpesa);
				if(response != null){
					responseData = "Thank you for your payment";
				}
				System.out.println("response " + response);
			}else{
				logger.info("Empty Parameter passed");
				responseData = "Empty Parameter passed";
			}
		}catch(Exception e){
			logger.error("Exception while storeTransactionDetails " + e);
			return responseData = e.getMessage();
		}
		return responseData;
	}


	@Override
	@Transactional
	public Collection<String> retriveAllTransactions() {
		Collection<String> transactionList = null;
		Client client = null;
		WebResource webResource = null;
		try{
			final String authenticationKey = loginIntoServerAndGetBase64EncodedAuthenticationKey();
			transactionList = this.mpesaBridgeRepository.retriveAllTransaction();
			for(String nationalId : transactionList){
				client = ClientHelper.createClient();
				webResource = client
				   .resource("https://localhost:8443/mifosng-provider/api/v1/search?query="+nationalId+"&resource=clientIdentifiers");
		 
				ClientResponse response = webResource.header("X-mifos-Platform-TenantId", "default")
							.header("Content-Type", "application/json")
							.header("Authorization","Basic "+authenticationKey)
					        .get(ClientResponse.class);
		 
				if (response.getStatus() != 200) {
				   throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
				}
		  
				String output = response.getEntity(String.class);
				JSONArray root = (JSONArray) JSONValue.parseWithException(output);
				JSONObject rootObj = (JSONObject) root.get(0);
				if(rootObj != null && rootObj.get("entityType").equals("CLIENTIDENTIFIER")){
					callClientMifosApi(rootObj.get("parentId").toString(),authenticationKey,nationalId);
				}else if(rootObj != null && rootObj.get("entityType").equals("SAVINGS")){
					
				}
			}
		}catch(Exception e){
			logger.error("Exception " + e);
		}
		return transactionList;
	}


	private String loginIntoServerAndGetBase64EncodedAuthenticationKey() {
		Client client = null;
		WebResource webResource = null;
		String authenticationKey = null;
		try{
			client = ClientHelper.createClient();
			webResource = client.resource(loginURL);
			 
					ClientResponse response = webResource.header("X-mifos-Platform-TenantId", "default")
								.header("Content-Type", "application/json")
						        .post(ClientResponse.class);
					String responseData = response.getEntity(String.class);
					JSONObject rootObj = (JSONObject) JSONValue.parseWithException(responseData);
					if(rootObj != null && !rootObj.equals("")){
						authenticationKey = rootObj.get("base64EncodedAuthenticationKey").toString();
					}
		}catch(Exception e){
			logger.error("Exception while loginIntoServerAndGetBase64EncodedAuthenticationKey " + e);
		}
		return authenticationKey;
	}


	private void callClientMifosApi(String clientId,String authenticationKey,String nationalId) {
		Client client = null;
		WebResource webResource = null;
		try{
			client = ClientHelper.createClient();
			webResource = client.resource("https://localhost:8443/mifosng-provider/api/v1/clients/"+clientId+"/accounts");
			 
					ClientResponse response = webResource.header("X-mifos-Platform-TenantId", "default")
								.header("Content-Type", "application/json")
								.header("Authorization","Basic "+authenticationKey)
						        .get(ClientResponse.class);
					String responseData = response.getEntity(String.class);
					/*JSONObject rootObj = (JSONObject) JSONValue.parseWithException(responseData);
					JSONArray loanAccountArray = (JSONArray) rootObj.get("loanAccounts");
					JSONArray savingAccountArray = (JSONArray) rootObj.get("savingsAccounts");*/
					final List<Mpesa> mpesaList = this.mpesaBridgeRepository.fetchTransactionInfoByNationalId(nationalId);
					for(Mpesa mpesa : mpesaList){
						mpesa.setStatus("R");
						mpesa.setClientId(Long.parseLong(clientId));
						this.mpesaBridgeRepository.save(mpesa);
					}
					/*if(loanAccountArray != null){
						JSONObject loanAccountNo = (JSONObject) loanAccountArray.get(0);
						if(loanAccountArray.size() <= 1){
							logger.info("Only one loan account do the transaction");
							//doTranasction(loanAccountNo.get("id").toString(),authenticationKey,nationalId);
						}else{
							logger.info("Multiple loan accounts ");
							//final List<Mpesa> mpesaList = this.mpesaBridgeRepository.fetchTransactionInfoByNationalId(nationalId);
							for(Mpesa mpesa : mpesaList){
								mpesa.setStatus("P");
								mpesa.setClientId(Long.parseLong(clientId));
								this.mpesaBridgeRepository.save(mpesa);
							}
						}
					}*/
					/*if(savingAccountArray != null){
						JSONObject savingAccountNo = (JSONObject) savingAccountArray.get(0);
						if(savingAccountNo.size() <= 1){
							logger.info("Only one saving account do the transaction");
							doTranasction(savingAccountNo.get("id").toString(),authenticationKey,nationalId);
						}else{
							logger.info("Multiple saving accounts ");
							//final List<Mpesa> mpesaList = this.mpesaBridgeRepository.fetchTransactionInfoByNationalId(nationalId);
							for(Mpesa mpesa : mpesaList){
								mpesa.setStatus("P");
								this.mpesaBridgeRepository.save(mpesa);
							}
						}
					}*/
					
		}catch(Exception e){
			logger.error("Exception while callClientMifosApi " + e);
		}
		
	}


	private void doTranasction(String mpesaId, String authenticationKey, String nationalId) {
		Client client = null;
		WebResource webResource = null;
		//List<Mpesa> mpesaList = null;
		try{
			/*if(nationalId != null){
				mpesaList = this.mpesaBridgeRepository.fetchTransactionInfoByNationalId(nationalId);
			}else{
				mpesaList = this.mpesaBridgeRepository.fetchTransactionInfoById(phoneNo);
			}*/
			List<Mpesa> mpesaList = this.mpesaBridgeRepository.fetchTransactionInfoByNationalId(nationalId);
			final Mpesa mpesa = mpesaList.get(0);
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMMM yyyy");
			LocalDate transactionDate = new LocalDate(mpesa.getTransactionDate());
			String date = transactionDate.toString(fmt);
			String json = "{\"transactionAmount\": "+mpesa.getTransactionAmount()+",\"transactionDate\": \""+date+"\",\"locale\": \"en\",\"dateFormat\": \"dd MMMM yyyy\"}";
			client = ClientHelper.createClient();
			webResource = client.resource("https://localhost:8443/mifosng-provider/api/v1/loans/"+mpesaId+"/transactions?command=repayment");
			 
					ClientResponse response = webResource.header("X-mifos-Platform-TenantId", "default")
								.header("Content-Type", "application/json")
								.header("Authorization","Basic "+authenticationKey)
						        .post(ClientResponse.class,json);
					String responseData = response.getEntity(String.class);
					logger.info("transaction " + responseData);
					mpesa.setStatus("S");
					this.mpesaBridgeRepository.save(mpesa);
			
		}catch(Exception e){
			logger.error("Exception while doTransaction " + e);
		}
		
	}


	@Override
	public Collection<Mpesa> retriveUnmappedTransactions() {
		Collection<Mpesa> unmappedTransactionList = null;
		try{
			unmappedTransactionList = this.mpesaBridgeRepository.retriveUnmappedTransactions();
		}catch(Exception e){
			logger.error("Exception while retriveUnmappedTransactions " + e);
		}
		return unmappedTransactionList;
	}
 

}
