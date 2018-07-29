package com.besafx.app.service;

import com.besafx.app.entity.OfferProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OfferProductService extends PagingAndSortingRepository<OfferProduct, Long>, JpaSpecificationExecutor<OfferProduct> {
    List<OfferProduct> findByOfferId(Long id);
    List<OfferProduct> findByProductId(Long id);
}
