package org.mifosplatform.mpesa.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "mpesa_recieved_txn_details")
public class Mpesa {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "ipn_id")
	private String ipnId;

	@Column(name = "orig")
	private String origin;

	@Column(name = "dest")
	private String destination;

	@Column(name = "client_id")
	private Long clientId;

	@Column(name = "client_name")
	private String clientName;

	@Column(name = "client_external_id")
	private String clientExternalId;

	@Column(name = "time_stamp")
	private String timeStamp;

	@Column(name = "text")
	private String testMessage;

	@Column(name = "mpesauser")
	private String user;

	@Column(name = "mpesapassword")
	private String password;

	@Column(name = "mpesa_transaction_code")
	private String transactionCode;

	@Column(name = "mpesa_msisdn")
	private String mobileNo;

	@Column(name = "mpesa_account_name")
	private String accountName;

	@Column(name = "mpesa_trx_date")
	@Temporal(TemporalType.DATE)
	private Date transactionDate;

	@Column(name = "mpesa_trx_time")
	private String transactionTime;

	@Column(name = "mpesa_amount")
	private BigDecimal transactionAmount;

	@Column(name = "mpesa_sender")
	private String sender;

	@Column(name = "mpesa_trx_type")
	private String type;

	@Column(name = "office_Id")
	private Long officeId;

	@Column(name = "status")
	private String status;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpnId() {
		return this.ipnId;
	}

	public void setIpnId(String ipnId) {
		this.ipnId = ipnId;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTestMessage() {
		return this.testMessage;
	}

	public void setTestMessage(String testMessage) {
		this.testMessage = testMessage;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTransactionCode() {
		return this.transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Date txnDate) {
		this.transactionDate = txnDate;
	}

	public String getTransactionTime() {
		return this.transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public BigDecimal getTransactionAmount() {
		return this.transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getClientExternalId() {
		return clientExternalId;
	}

	public void setClientExternalId(String clientExternalId) {
		this.clientExternalId = clientExternalId;
	}

}
