package com.besafx.app.report;

import com.besafx.app.async.AsyncOfferGenerator;
import com.besafx.app.component.ReportExporter;
import com.besafx.app.enums.ExportType;
import com.besafx.app.init.Initializer;
import com.besafx.app.service.OfferService;
import com.besafx.app.util.CompanyOptions;
import com.besafx.app.util.JSONConverter;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReportOfferController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportOfferController.class);

    @Autowired
    private AsyncOfferGenerator asyncOfferGenerator;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/offer", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printOffer(@RequestParam(value = "offerId") Long offerId, HttpServletResponse response) throws Exception {
        reportExporter.export("عرض سعر", ExportType.PDF, response, asyncOfferGenerator.generate(offerId).get());
    }
}
