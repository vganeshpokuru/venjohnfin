package org.mifosplatform.mpesa.repository;

import java.util.Collection;
import java.util.List;
import org.mifosplatform.mpesa.domain.Mpesa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MpesaBridgeRepository extends CrudRepository<Mpesa, Long>{

	@Query("select mpesa.accountName from Mpesa mpesa where mpesa.status = 'R'")
	Collection<String> retriveAllTransaction();

	@Query("from Mpesa mpesa WHERE mpesa.mobileNo = :phoneNo AND mpesa.status = 'R'")
	List<Mpesa> fetchTransactionInfoById(@Param("phoneNo") String phoneNo);

	@Query("from Mpesa mpesa WHERE mpesa.status = 'R'")
	Collection<Mpesa> retriveUnmappedTransactions();

	@Query("from Mpesa mpesa WHERE mpesa.accountName = :nationalId AND mpesa.status = 'R'")
	List<Mpesa> fetchTransactionInfoByNationalId(@Param("nationalId") String nationalId);
}
