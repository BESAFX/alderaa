package com.besafx.app.service;

import com.besafx.app.entity.SupplierPayment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public interface SupplierPaymentService extends PagingAndSortingRepository<SupplierPayment, Long>, JpaSpecificationExecutor<SupplierPayment> {

    SupplierPayment findTopByOrderByCodeDesc();

    SupplierPayment findByCodeAndIdIsNot(Integer code, Long id);

    List<SupplierPayment> findBySupplierId(Long id);

    List<SupplierPayment> findByBankTransactionDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
}
