package com.besafx.app.search;

import com.besafx.app.entity.OrderSell;
import com.besafx.app.service.OrderSellService;
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
public class OrderSellSearch {

    private static final Logger LOG = LoggerFactory.getLogger(OrderSellSearch.class);

    @Autowired
    private OrderSellService orderSellService;

    public Page<OrderSell> filter(
            //OrderSell Filters
            final Integer codeFrom,
            final Integer codeTo,
            final Long writtenDateFrom,
            final Long writtenDateTo,
            //Customer Filters
            final Integer customerCodeFrom,
            final Integer customerCodeTo,
            final Long customerRegisterDateFrom,
            final Long customerRegisterDateTo,
            final String customerName,
            final String customerMobile,
            final String filterCompareType,
            Pageable pageRequest
                                    ) {

        List<Specification<OrderSell>> predicates = new ArrayList<>();
        //OrderSell Specification
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(writtenDateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("writtenDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(writtenDateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("writtenDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        //Customer Specification
        Optional.ofNullable(customerCodeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("customer").get("code"), value)));
        Optional.ofNullable(customerCodeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("customer").get("code"), value)));
        Optional.ofNullable(customerRegisterDateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("customer").get("registerDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(customerRegisterDateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("customer").get("registerDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(customerName).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("customer").get("contact").get("name"), "%" + value + "%")));
        Optional.ofNullable(customerMobile).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("customer").get("contact").get("mobile"), "%" + value + "%")));

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
            return orderSellService.findAll(result, pageRequest);
        } else {
            return orderSellService.findAll(pageRequest);
        }
    }
}
