package com.besafx.app.service;

import com.besafx.app.entity.CustomerPayment;
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
public interface CustomerPaymentService extends PagingAndSortingRepository<CustomerPayment, Long>, JpaSpecificationExecutor<CustomerPayment> {

    CustomerPayment findTopByOrderByCodeDesc();

    CustomerPayment findByCodeAndIdIsNot(Integer code, Long id);

    List<CustomerPayment> findByCustomerId(Long id);

    List<CustomerPayment> findByBankTransactionDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
}
