package com.besafx.app.report;

import com.besafx.app.component.ReportExporter;
import com.besafx.app.entity.BillPurchase;
import com.besafx.app.enums.ExportType;
import com.besafx.app.service.BillPurchaseProductService;
import com.besafx.app.service.BillPurchaseService;
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
public class ReportBillPurchaseController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportBillPurchaseController.class);

    @Autowired
    private BillPurchaseService billPurchaseService;

    @Autowired
    private BillPurchaseProductService billPurchaseProductService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/billPurchase", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printBillPurchase(
            @RequestParam(value = "billPurchaseId") Long billPurchaseId,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("BILL_PURCHASE", billPurchaseService.findOne(billPurchaseId));

        ClassPathResource jrxmlFile = new ClassPathResource("/report/billPurchase/BillPurchase.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("فاتورة شراء", ExportType.PDF, response, jasperPrint);
    }

    @RequestMapping(value = "/report/purchasesBySupplier", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printPurchasesBySupplier(
            @RequestParam(value = "exportType") ExportType exportType,
            @RequestParam(value = "dateFrom", required = false) Long dateFrom,
            @RequestParam(value = "dateTo", required = false) Long dateTo,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        List<Specification<BillPurchase>> predicates = new ArrayList<>();
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("writtenDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("writtenDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            map.put("BILL_PURCHASES", billPurchaseService.findAll(result, new Sort(Sort.Direction.ASC, "supplier.contact.name")));
        }else{
            map.put("BILL_PURCHASES", billPurchaseService.findAll(new Sort(Sort.Direction.ASC, "supplier.contact.name")));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---"  : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---"  : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/billPurchase/PurchasesBySupplier.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير المشتريات حسب المورد", exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/purchasesByProduct", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printPurchasesByProduct(
            @RequestParam(value = "exportType") ExportType exportType,
            @RequestParam(value = "dateFrom", required = false) Long dateFrom,
            @RequestParam(value = "dateTo", required = false) Long dateTo,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        List<Specification<BillPurchase>> predicates = new ArrayList<>();
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("billPurchase").get("writtenDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("billPurchase").get("writtenDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            map.put("BILL_PURCHASE_PRODUCT", billPurchaseProductService.findAll(result, new Sort(Sort.Direction.ASC, "product.id")));
        }else{
            map.put("BILL_PURCHASE_PRODUCT", billPurchaseProductService.findAll(new Sort(Sort.Direction.ASC, "product.id")));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---"  : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---"  : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/billPurchase/PurchasesByProduct.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير المشتريات حسب المنتج", exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/purchasesByPerson", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printPurchasesByPerson(
            @RequestParam(value = "exportType") ExportType exportType,
            @RequestParam(value = "dateFrom", required = false) Long dateFrom,
            @RequestParam(value = "dateTo", required = false) Long dateTo,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        List<Specification<BillPurchase>> predicates = new ArrayList<>();
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("writtenDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("writtenDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            map.put("BILL_PURCHASES", billPurchaseService.findAll(result, new Sort(Sort.Direction.ASC, "person.contact.name")));
        }else{
            map.put("BILL_PURCHASES", billPurchaseService.findAll(new Sort(Sort.Direction.ASC, "person.contact.name")));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---"  : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---"  : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/billPurchase/PurchasesByPerson.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير المشتريات حسب المستخدم", exportType, response, jasperPrint);
    }
}
