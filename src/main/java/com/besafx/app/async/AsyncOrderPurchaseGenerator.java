package com.besafx.app.async;

import com.besafx.app.entity.OrderPurchase;
import com.besafx.app.service.OrderPurchaseService;
import com.besafx.app.util.DateConverter;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.Future;

@Service
public class AsyncOrderPurchaseGenerator {

    private final Logger log = LoggerFactory.getLogger(AsyncOrderPurchaseGenerator.class);

    @Autowired
    private OrderPurchaseService orderPurchaseService;

    @Async("threadMultiplePool")
    @Transactional
    public Future<JasperPrint> generateOrderPurchase(Long orderPurchaseId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("ORDER_PURCHASE", orderPurchaseService.findOne(orderPurchaseId));

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        try {
            ClassPathResource jrxmlFile = new ClassPathResource("/report/orderPurchase/OrderPurchase.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
            return new AsyncResult<>(jasperPrint);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AsyncResult<>(null);
        }
    }

    @Async("threadMultiplePool")
    @Transactional
    public Future<JasperPrint> generateOrderPurchaseDetails(Long dateFrom, Long dateTo) throws Exception {
        List<OrderPurchase> orderPurchases;
        List<Specification<OrderPurchase>> predicates = new ArrayList<>();
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("writtenDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("writtenDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            orderPurchases = orderPurchaseService.findAll(result, new Sort(Sort.Direction.ASC, "writtenDate"));
        } else {
            orderPurchases = Lists.newArrayList(orderPurchaseService.findAll(new Sort(Sort.Direction.ASC, "writtenDate")));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---" : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---" : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());

        try {
            ClassPathResource jrxmlFile = new ClassPathResource("/report/orderPurchase/OrderPurchasesDetails.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(orderPurchases));
            return new AsyncResult<>(jasperPrint);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AsyncResult<>(null);
        }
    }

    public byte[] getOrderPurchaseFile(Long orderPurchaseId) throws Exception {
        return JasperExportManager.exportReportToPdf(generateOrderPurchase(orderPurchaseId).get());
    }

    public byte[] getOrderPurchaseDetailsFile(Long dateFrom, Long dateTo) throws Exception {
        return JasperExportManager.exportReportToPdf(generateOrderPurchaseDetails(dateFrom, dateTo).get());
    }
}
