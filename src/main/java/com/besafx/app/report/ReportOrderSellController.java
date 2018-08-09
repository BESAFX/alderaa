package com.besafx.app.report;

import com.besafx.app.component.ReportExporter;
import com.besafx.app.entity.OrderSell;
import com.besafx.app.enums.ExportType;
import com.besafx.app.service.OrderSellService;
import com.besafx.app.util.DateConverter;
import com.google.common.collect.Lists;
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

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

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

        Map<String, Object> map = new HashMap<>();
        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        StringBuilder builder = new StringBuilder();
        builder.append("الفترة من ");
        builder.append(dateFrom == null ? "---"  : DateConverter.getDateInFormat(dateFrom));
        builder.append(" إلى الفترة ");
        builder.append(dateTo == null ? "---"  : DateConverter.getDateInFormat(dateTo));
        map.put("REPORT_HEADER_SUB_TITLE", builder.toString());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/orderSell/OrderSellsDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(orderSells));
        reportExporter.export("كشف مفصل لأوامر البيع", exportType, response, jasperPrint);
    }
}
