package com.besafx.app.report;

import com.besafx.app.component.ReportExporter;
import com.besafx.app.enums.ExportType;
import com.besafx.app.service.CustomerService;
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
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReportCustomerController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportCustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/customersInfoData", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printCustomersInfoData(
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("CUSTOMERS", customerService.findAll(new Sort(Sort.Direction.ASC, "contact.name")));

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/customer/CustomersInfoData.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير بيانات العملاء", exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/customersBalanceData", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printCustomersBalanceData(
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("CUSTOMERS", customerService.findAll(new Sort(Sort.Direction.ASC, "contact.name")));

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/customer/CustomersBalanceData.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير أرصدة العملاء", exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/customerStatement/{customerId}", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printCustomerStatement(
            @PathVariable(value = "customerId") Long customerId,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/customer/CustomerStatement.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(Lists.newArrayList(customerService
                                                                                                                                            .findOne(customerId))));
        reportExporter.export("كشف حساب عميل", exportType, response, jasperPrint);
    }
}
