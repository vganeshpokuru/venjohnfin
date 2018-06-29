package org.mifosplatform.mpesa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_ext_txn")
public class MpesaTransaction {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="txn_json")
	private String txnJson;
	
	@Column(name="status")
	private String status;

	public String getTxnJson() {
		return this.txnJson;
	}

	public void setTxnJson(String txnJson) {
		this.txnJson = txnJson;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getId() {
		return this.id;
	}
	
}
