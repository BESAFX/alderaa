package com.besafx.app.report;

import com.besafx.app.async.AsyncOrderPurchaseGenerator;
import com.besafx.app.component.ReportExporter;
import com.besafx.app.enums.ExportType;
import com.besafx.app.init.Initializer;
import com.besafx.app.service.OrderPurchaseService;
import com.besafx.app.util.CompanyOptions;
import com.besafx.app.util.JSONConverter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
public class ReportOrderPurchaseController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportOrderPurchaseController.class);

    @Autowired
    private AsyncOrderPurchaseGenerator asyncOrderPurchaseGenerator;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/orderPurchase", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printOrderPurchase(@RequestParam(value = "orderPurchaseId") Long orderPurchaseId, HttpServletResponse response) throws Exception {
        reportExporter.export("أمر شراء", ExportType.PDF, response, asyncOrderPurchaseGenerator.generate(orderPurchaseId).get());
    }
}
