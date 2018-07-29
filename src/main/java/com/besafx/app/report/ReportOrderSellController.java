package com.besafx.app.report;

import com.besafx.app.component.ReportExporter;
import com.besafx.app.entity.OrderSell;
import com.besafx.app.enums.ExportType;
import com.besafx.app.init.Initializer;
import com.besafx.app.service.OrderSellService;
import com.besafx.app.util.CompanyOptions;
import com.besafx.app.util.DateConverter;
import com.besafx.app.util.JSONConverter;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@RestController
public class ReportOrderSellController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportOrderSellController.class);

    @Autowired
    private OrderSellService orderSellService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/orderSell", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printOrderSell(
            @RequestParam(value = "orderSellId") Long orderSellId,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("ORDER_SELL", orderSellService.findOne(orderSellId));

        CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
        map.put("REPORT_TITLE", options.getReportTitle());
        map.put("REPORT_SUB_TITLE", options.getReportSubTitle());
        map.put("REPORT_FOOTER", options.getReportFooter());
        map.put("LOGO", options.getLogo());
        map.put("BACKGROUND", options.getBackground());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/orderSell/OrderSell.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("أمر بيع", ExportType.PDF, response, jasperPrint);
    }

    @RequestMapping(value = "/report/orderSellsDetails", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printOrderSellsDetails(
            @RequestParam(value = "exportType") ExportType exportType,
            @RequestParam(value = "dateFrom", required = false) Long dateFrom,
            @RequestParam(value = "dateTo", required = false) Long dateTo,
            HttpServletResponse response) throws Exception {

        List<OrderSell> orderSells;
        List<Specification<OrderSell>> predicates = new ArrayList<>();
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("writtenDate"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("writtenDate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            orderSells = orderSellService.findAll(result, new Sort(Sort.Direction.ASC, "writtenDate"));
        }else{
            orderSells = Lists.newArrayList(orderSellService.findAll(new Sort(Sort.Direction.ASC, "writtenDate")));
        }

        CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
        Map<String, Object> map = new HashMap<>();
        map.put("REPORT_TITLE", options.getReportTitle());
        map.put("REPORT_SUB_TITLE", options.getReportSubTitle());
        map.put("REPORT_FOOTER", options.getReportFooter());

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---"  : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---"  : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());
        map.put("LOGO", options.getLogo());
        map.put("BACKGROUND", options.getBackground());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/orderSell/OrderSellsDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(orderSells));
        reportExporter.export("كشف مفصل لأوامر البيع", exportType, response, jasperPrint);
    }
}
