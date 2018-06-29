package org.mifosplatform.mpesa.repository;

import org.mifosplatform.mpesa.domain.MpesaBranchMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MpesaBranchMappingRepository
		extends
			JpaRepository<MpesaBranchMapping, Long>,
			JpaSpecificationExecutor<MpesaBranchMapping> {

	@Query("from MpesaBranchMapping mbm where mbm.mpesaPayBillNumber =:mpesaPayBillNumber ")
	MpesaBranchMapping getOfficeIdFromDestNumber(
			@Param("mpesaPayBillNumber") String mpesaPayBillNumber);

}
