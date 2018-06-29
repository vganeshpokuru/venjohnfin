package org.mifosplatform.mpesa.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import org.mifosplatform.mpesa.domain.Mpesa;

public interface MpesaBridgeService {
	
	public Collection<String> retriveAllTransactions();

	public String storeTransactionDetails(final Long id, final String origin, final String dest,final String tStamp, final String text, final String user, 
			final String pass, final String mpesaCode, final String mpesaAccount, final String mobileNo,final Date txnDate, final String txnTime, 
			final BigDecimal mpesaAmount, final String sender);

	public Collection<Mpesa> retriveUnmappedTransactions();
}
