package com.besafx.app.async;

import com.besafx.app.service.OfferService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class AsyncOfferGenerator {

    private final Logger log = LoggerFactory.getLogger(AsyncOfferGenerator.class);

    @Autowired
    private OfferService offerService;

    @Async("threadMultiplePool")
    @Transactional
    public Future<JasperPrint> generate(Long offerId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        try {
            ClassPathResource jrxmlFile = new ClassPathResource("/report/offer/Offer.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(Lists.newArrayList
                    (offerService.findOne(offerId))));
            return new AsyncResult<>(jasperPrint);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AsyncResult<>(null);
        }
    }

    public byte[] getFile(Long offerId) throws Exception {
        return JasperExportManager.exportReportToPdf(generate(offerId).get());
    }
}
