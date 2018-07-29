package com.besafx.app.service;

import com.besafx.app.entity.Offer;
import com.besafx.app.entity.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OfferService extends PagingAndSortingRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {

    Offer findTopByOrderByCodeDesc();

    Offer findByCode(Long code);

    Offer findByCodeAndIdIsNot(Long code, Long id);

    List<Offer> findByCustomer(Customer customer);
}
