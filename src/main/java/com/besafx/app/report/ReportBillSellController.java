package com.besafx.app.report;

import com.besafx.app.component.ReportExporter;
import com.besafx.app.entity.BillSell;
import com.besafx.app.enums.ExportType;
import com.besafx.app.service.BillSellProductService;
import com.besafx.app.service.BillSellService;
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
public class ReportBillSellController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportBillSellController.class);

    @Autowired
    private BillSellService billSellService;

    @Autowired
    private BillSellProductService billSellProductService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/billSell", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printBillSell(
            @RequestParam(value = "billSellId") Long billSellId,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("BILL_SELL", billSellService.findOne(billSellId));
        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/BillSell.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("فاتورة بيع", ExportType.PDF, response, jasperPrint);
    }

    @RequestMapping(value = "/report/salesByCustomer", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printSalesByCustomer(
            @RequestParam(value = "exportType") ExportType exportType,
            @RequestParam(value = "dateFrom", required = false) Long dateFrom,
            @RequestParam(value = "dateTo", required = false) Long dateTo,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        List<Specification<BillSell>> predicates = new ArrayList<>();
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("writtenDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("writtenDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            map.put("BILL_SELLS", billSellService.findAll(result, new Sort(Sort.Direction.ASC, "customer.contact.name")));
        }else{
            map.put("BILL_SELLS", billSellService.findAll(new Sort(Sort.Direction.ASC, "customer.contact.name")));
        }

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---"  : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---"  : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/SalesByCustomer.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير المبيعات الزبون", exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/salesByProduct", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printSalesByProduct(
            @RequestParam(value = "exportType") ExportType exportType,
            @RequestParam(value = "dateFrom", required = false) Long dateFrom,
            @RequestParam(value = "dateTo", required = false) Long dateTo,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        List<Specification<BillSell>> predicates = new ArrayList<>();
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("billSell").get("writtenDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("billSell").get("writtenDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            map.put("BILL_SELL_PRODUCT", billSellProductService.findAll(result, new Sort(Sort.Direction.ASC, "product.id")));
        }else{
            map.put("BILL_SELL_PRODUCT", billSellProductService.findAll(new Sort(Sort.Direction.ASC, "product.id")));
        }

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---"  : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---"  : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/SalesByProduct.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير المبيعات المنتج", exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/salesByPerson", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printSalesByPerson(
            @RequestParam(value = "exportType") ExportType exportType,
            @RequestParam(value = "dateFrom", required = false) Long dateFrom,
            @RequestParam(value = "dateTo", required = false) Long dateTo,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        List<Specification<BillSell>> predicates = new ArrayList<>();
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("writtenDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("writtenDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            map.put("BILL_SELLS", billSellService.findAll(result, new Sort(Sort.Direction.ASC, "person.contact.name")));
        }else{
            map.put("BILL_SELLS", billSellService.findAll(new Sort(Sort.Direction.ASC, "person.contact.name")));
        }

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---"  : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---"  : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/SalesByPerson.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير المبيعات المستخدم", exportType, response, jasperPrint);
    }
}
