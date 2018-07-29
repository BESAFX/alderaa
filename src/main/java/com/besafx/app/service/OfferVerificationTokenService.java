package com.besafx.app.service;

import com.besafx.app.entity.Offer;
import com.besafx.app.entity.OfferVerificationToken;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface OfferVerificationTokenService extends
        PagingAndSortingRepository<OfferVerificationToken, Long>,
        JpaSpecificationExecutor<OfferVerificationToken> {

    OfferVerificationToken findByToken(String token);

    OfferVerificationToken findByOffer(Offer offer);

    OfferVerificationToken findByOfferAndToken(Offer offer, String token);
}
