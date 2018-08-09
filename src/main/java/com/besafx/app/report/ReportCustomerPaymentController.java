package com.besafx.app.report;

import com.besafx.app.component.ReportExporter;
import com.besafx.app.entity.CustomerPayment;
import com.besafx.app.enums.ExportType;
import com.besafx.app.service.CustomerPaymentService;
import com.besafx.app.util.DateConverter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class ReportCustomerPaymentController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportCustomerPaymentController.class);

    @Autowired
    private CustomerPaymentService customerPaymentService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/incomesByCustomer", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printIncomesByCustomer(
            @RequestParam(value = "exportType") ExportType exportType,
            @RequestParam(value = "dateFrom", required = false) Long dateFrom,
            @RequestParam(value = "dateTo", required = false) Long dateTo,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        List<Specification<CustomerPayment>> predicates = new ArrayList<>();
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("data"), new DateTime
                (value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("data"), new DateTime(value)
                .plusDays(1).withTimeAtStartOfDay().toDate())));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            map.put("CUSTOMER_PAYMENTS", customerPaymentService.findAll(result, new Sort(Sort.Direction.ASC, "customer.contact.name")));
        } else {
            map.put("CUSTOMER_PAYMENTS", customerPaymentService.findAll(new Sort(Sort.Direction.ASC, "customer.contact.name")));
        }

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---" : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---" : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/customerPayment/IncomesByCustomer.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير الايرادات حسب العميل", exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/incomesByPerson", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printIncomesByPerson(
            @RequestParam(value = "exportType") ExportType exportType,
            @RequestParam(value = "dateFrom", required = false) Long dateFrom,
            @RequestParam(value = "dateTo", required = false) Long dateTo,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        List<Specification<CustomerPayment>> predicates = new ArrayList<>();
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("data"), new DateTime
                (value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("data"), new DateTime(value)
                .plusDays(1).withTimeAtStartOfDay().toDate())));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            map.put("CUSTOMER_PAYMENTS", customerPaymentService.findAll(result, new Sort(Sort.Direction.ASC, "person.contact.name")));
        } else {
            map.put("CUSTOMER_PAYMENTS", customerPaymentService.findAll(new Sort(Sort.Direction.ASC, "person.contact.name")));
        }

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---" : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---" : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/customerPayment/IncomesByPerson.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير الايرادات حسب الزبون", exportType, response, jasperPrint);
    }
}
