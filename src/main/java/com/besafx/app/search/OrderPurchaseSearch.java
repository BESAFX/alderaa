package com.besafx.app.search;

import com.besafx.app.entity.OrderPurchase;
import com.besafx.app.service.OrderPurchaseService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderPurchaseSearch {

    private static final Logger LOG = LoggerFactory.getLogger(OrderPurchaseSearch.class);

    @Autowired
    private OrderPurchaseService orderPurchaseService;

    public Page<OrderPurchase> filter(
            //OrderPurchase Filters
            final Integer codeFrom,
            final Integer codeTo,
            final Long writtenDateFrom,
            final Long writtenDateTo,
            //Supplier Filters
            final Integer supplierCodeFrom,
            final Integer supplierCodeTo,
            final Long supplierRegisterDateFrom,
            final Long supplierRegisterDateTo,
            final String supplierName,
            final String supplierMobile,
            final String filterCompareType,
            Pageable pageRequest
                                    ) {

        List<Specification<OrderPurchase>> predicates = new ArrayList<>();
        //OrderPurchase Specification
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(writtenDateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("writtenDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(writtenDateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("writtenDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        //Supplier Specification
        Optional.ofNullable(supplierCodeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("supplier").get("code"), value)));
        Optional.ofNullable(supplierCodeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("supplier").get("code"), value)));
        Optional.ofNullable(supplierRegisterDateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("supplier").get("registerDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(supplierRegisterDateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("supplier").get("registerDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(supplierName).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("supplier").get("contact").get("name"), "%" + value + "%")));
        Optional.ofNullable(supplierMobile).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("supplier").get("contact").get("mobile"), "%" + value + "%")));

        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                if (filterCompareType == null) {
                    result = Specifications.where(result).and(predicates.get(i));
                    continue;
                }
                result = filterCompareType.equalsIgnoreCase("and") ? Specifications.where(result).and(predicates.get(i)) : Specifications.where
                        (result).or(predicates.get(i));
            }
            return orderPurchaseService.findAll(result, pageRequest);
        } else {
            return orderPurchaseService.findAll(pageRequest);
        }
    }
}
