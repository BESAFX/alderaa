package com.besafx.app.service;

import com.besafx.app.entity.OrderPurchaseProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderPurchaseProductService extends PagingAndSortingRepository<OrderPurchaseProduct, Long>, JpaSpecificationExecutor<OrderPurchaseProduct> {
    List<OrderPurchaseProduct> findByOrderPurchaseId(Long id);
    List<OrderPurchaseProduct> findByProductId(Long id);
}
