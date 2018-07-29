package com.besafx.app.async;

import com.besafx.app.init.Initializer;
import com.besafx.app.service.OfferService;
import com.besafx.app.service.OrderPurchaseService;
import com.besafx.app.util.CompanyOptions;
import com.besafx.app.util.JSONConverter;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class AsyncOrderPurchaseGenerator {

    private final Logger log = LoggerFactory.getLogger(AsyncOrderPurchaseGenerator.class);

    @Autowired
    private OrderPurchaseService orderPurchaseService;

    @Async("threadMultiplePool")
    @Transactional
    public Future<JasperPrint> generate(Long orderPurchaseId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("ORDER_PURCHASE", orderPurchaseService.findOne(orderPurchaseId));

        CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
        map.put("REPORT_TITLE", options.getReportTitle());
        map.put("REPORT_SUB_TITLE", options.getReportSubTitle());
        map.put("REPORT_FOOTER", options.getReportFooter());
        map.put("LOGO", options.getLogo());
        map.put("BACKGROUND", options.getBackground());

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

    public byte[] getFile(Long orderPurchaseId) throws Exception {
        return JasperExportManager.exportReportToPdf(generate(orderPurchaseId).get());
    }
}
