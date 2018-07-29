package com.besafx.app.service;

import com.besafx.app.entity.Offer;
import com.besafx.app.entity.OfferAttach;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OfferAttachService extends PagingAndSortingRepository<OfferAttach, Long>, JpaSpecificationExecutor<OfferAttach> {
    List<OfferAttach> findByOffer(Offer id);
    List<OfferAttach> findByOfferId(Long id);
}
