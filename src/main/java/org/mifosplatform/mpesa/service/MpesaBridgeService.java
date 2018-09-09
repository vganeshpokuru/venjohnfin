package org.mifosplatform.mpesa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.mifosplatform.mpesa.domain.Mpesa;
import org.springframework.data.domain.Page;

public interface MpesaBridgeService {

	// public ArrayList<Mpesa> retriveAllTransactions(Long officeId);

	public String storeTransactionDetails(final String id, final String origin,
			final String dest, final String tStamp, final String text,
			final String user, final String pass, final String mpesaCode,
			final String mpesaAccount, final String mobileNo,
			final String txnDate, final String txnTime,
			final BigDecimal mpesaAmount, final String sender,
			final String mpesaTxnType, final Long officeId);

	public Page<Mpesa> retriveUnmappedTransactions(Long officeId,
			Integer offset, Integer limit);

	public List<Mpesa> Payment(Long Id, Long officeId, Long ClientId);

	public Page<Mpesa> searchMpesaDetail(String status, String mobileNo,
			Date fromDate, Date toDate, Long officeId, Integer offset,
			Integer limit);

	public String branchMap(String MobileNo, String accountNo, Long officeId);

}
