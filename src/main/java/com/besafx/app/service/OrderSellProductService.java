package com.besafx.app.service;

import com.besafx.app.entity.OrderSellProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderSellProductService extends PagingAndSortingRepository<OrderSellProduct, Long>, JpaSpecificationExecutor<OrderSellProduct> {
    List<OrderSellProduct> findByOrderSellId(Long id);
    List<OrderSellProduct> findByProductId(Long id);
}
