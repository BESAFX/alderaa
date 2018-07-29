package com.besafx.app.service;

import com.besafx.app.entity.OrderPurchase;
import com.besafx.app.entity.Supplier;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderPurchaseService extends PagingAndSortingRepository<OrderPurchase, Long>, JpaSpecificationExecutor<OrderPurchase> {

    OrderPurchase findTopByOrderByCodeDesc();

    OrderPurchase findByCodeAndIdIsNot(Long code, Long id);

    OrderPurchase findByCode(Long code);

    List<OrderPurchase> findBySupplier(Supplier supplier);
}
