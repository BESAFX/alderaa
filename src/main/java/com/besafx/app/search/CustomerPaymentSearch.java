package com.besafx.app.search;

import com.besafx.app.entity.CustomerPayment;
import com.besafx.app.service.CustomerPaymentService;
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
public class CustomerPaymentSearch {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerPaymentSearch.class);

    @Autowired
    private CustomerPaymentService customerPaymentService;

    public Page<CustomerPayment> filter(
            //CustomerPayment Filters
            final Long dateFrom,
            final Long dateTo,
            //Customer Filters
            final String customerName,
            final String customerMobile,
            final String filterCompareType,
            Pageable pageRequest) {

        List<Specification<CustomerPayment>> predicates = new ArrayList<>();
        //CustomerPayment Specification
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("bankTransaction").get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("bankTransaction").get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        //Customer Specification
        Optional.ofNullable(customerName).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("billSell").get("customer").get("contact").get("name"), "%" + value + "%")));
        Optional.ofNullable(customerMobile).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("billSell").get("customer").get("contact").get("mobile"), "%" + value + "%")));

        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                if (filterCompareType == null) {
                    result = Specifications.where(result).and(predicates.get(i));
                    continue;
                }
                result = filterCompareType.equalsIgnoreCase("and") ?
                        Specifications.where(result).and(predicates.get(i)) :
                        Specifications.where(result).or(predicates.get(i));
            }
            return customerPaymentService.findAll(result, pageRequest);
        } else {
            return customerPaymentService.findAll(pageRequest);
        }
    }
}
