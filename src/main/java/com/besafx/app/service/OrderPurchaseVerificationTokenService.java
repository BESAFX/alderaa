package com.besafx.app.service;

import com.besafx.app.entity.OrderPurchase;
import com.besafx.app.entity.OrderPurchaseVerificationToken;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface OrderPurchaseVerificationTokenService extends
        PagingAndSortingRepository<OrderPurchaseVerificationToken, Long>,
        JpaSpecificationExecutor<OrderPurchaseVerificationToken> {

    OrderPurchaseVerificationToken findByToken(String token);

    OrderPurchaseVerificationToken findByOrderPurchase(OrderPurchase offer);

    OrderPurchaseVerificationToken findByOrderPurchaseAndToken(OrderPurchase offer, String token);
}
