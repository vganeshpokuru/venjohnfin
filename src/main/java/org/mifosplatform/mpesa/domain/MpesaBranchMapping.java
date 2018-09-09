package org.mifosplatform.mpesa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mpesa_txn_branch_mapping")
public class MpesaBranchMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "office_id")
	private Long office_id;

	@Column(name = "mpesa_pay_bill_number")
	private String mpesaPayBillNumber;

	public Long getOffice_id() {
		return office_id;
	}

	public void setOffice_id(Long office_id) {
		this.office_id = office_id;
	}

	public String getMpesaPayBillNumber() {
		return mpesaPayBillNumber;
	}

	public void setMpesaPayBillNumber(String mpesaPayBillNumber) {
		this.mpesaPayBillNumber = mpesaPayBillNumber;
	}

}
