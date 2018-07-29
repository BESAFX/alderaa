package com.besafx.app.service;

import com.besafx.app.entity.OrderSell;
import com.besafx.app.entity.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderSellService extends PagingAndSortingRepository<OrderSell, Long>, JpaSpecificationExecutor<OrderSell> {

    OrderSell findTopByOrderByCodeDesc();

    OrderSell findByCodeAndIdIsNot(Long code, Long id);

    OrderSell findByCode(Long code);

    List<OrderSell> findByCustomer(Customer customer);
}
