package com.besafx.app.search;

import com.besafx.app.entity.SupplierPayment;
import com.besafx.app.service.SupplierPaymentService;
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
public class SupplierPaymentSearch {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierPaymentSearch.class);

    @Autowired
    private SupplierPaymentService supplierPaymentService;

    public Page<SupplierPayment> filter(
            //SupplierPayment Filters
            final Long dateFrom,
            final Long dateTo,
            //Supplier Filters
            final String supplierName,
            final String supplierMobile,
            final String filterCompareType,
            Pageable pageRequest) {

        List<Specification<SupplierPayment>> predicates = new ArrayList<>();
        //SupplierPayment Specification
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("bankTransaction").get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("bankTransaction").get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        //Supplier Specification
        Optional.ofNullable(supplierName).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("billPurchase").get("supplier").get("contact").get("name"), "%" + value + "%")));
        Optional.ofNullable(supplierMobile).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("billPurchase").get("supplier").get("contact").get("mobile"), "%" + value + "%")));

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
            return supplierPaymentService.findAll(result, pageRequest);
        } else {
            return supplierPaymentService.findAll(pageRequest);
        }
    }
}
