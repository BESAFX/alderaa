package com.besafx.app.report;

import com.besafx.app.async.AsyncOrderPurchaseGenerator;
import com.besafx.app.component.ReportExporter;
import com.besafx.app.enums.ExportType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ReportOrderPurchaseController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportOrderPurchaseController.class);

    @Autowired
    private AsyncOrderPurchaseGenerator asyncOrderPurchaseGenerator;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/orderPurchase", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printOrderPurchase(
            @RequestParam(value = "orderPurchaseId") Long orderPurchaseId,
            HttpServletResponse response) throws Exception {
        reportExporter.export("أمر شراء", ExportType.PDF, response, asyncOrderPurchaseGenerator.generateOrderPurchase(orderPurchaseId).get());
    }

    @RequestMapping(value = "/report/orderPurchasesDetails", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printOrderPurchasesDetails(
            @RequestParam(value = "exportType") ExportType exportType,
            @RequestParam(value = "dateFrom", required = false) Long dateFrom,
            @RequestParam(value = "dateTo", required = false) Long dateTo,
            HttpServletResponse response) throws Exception {
        reportExporter.export("كشف مفصل لأوامر الشراء", exportType, response, asyncOrderPurchaseGenerator.generateOrderPurchaseDetails(dateFrom, dateTo).get());
    }
}
