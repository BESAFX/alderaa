package com.besafx.app.report;

import com.besafx.app.component.ReportExporter;
import com.besafx.app.entity.Customer;
import com.besafx.app.enums.ExportType;
import com.besafx.app.init.Initializer;
import com.besafx.app.service.CustomerService;
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
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
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

        CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
        map.put("REPORT_TITLE", options.getReportTitle());
        map.put("REPORT_SUB_TITLE", options.getReportSubTitle());
        map.put("REPORT_FOOTER", options.getReportFooter());
        map.put("LOGO", options.getLogo());
        map.put("BACKGROUND", options.getBackground());

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

        CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
        map.put("REPORT_TITLE", options.getReportTitle());
        map.put("REPORT_SUB_TITLE", options.getReportSubTitle());
        map.put("REPORT_FOOTER", options.getReportFooter());
        map.put("LOGO", options.getLogo());
        map.put("BACKGROUND", options.getBackground());

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
        CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
        map.put("REPORT_TITLE", options.getReportTitle());
        map.put("REPORT_SUB_TITLE", options.getReportSubTitle());
        map.put("REPORT_FOOTER", options.getReportFooter());
        map.put("LOGO", options.getLogo());
        map.put("BACKGROUND", options.getBackground());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/customer/CustomerStatement.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(Lists.newArrayList(customerService
                                                                                                                                            .findOne(customerId))));
        reportExporter.export("كشف حساب عميل", exportType, response, jasperPrint);
    }
}
