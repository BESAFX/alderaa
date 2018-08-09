package com.besafx.app.report;

import com.besafx.app.component.ReportExporter;
import com.besafx.app.enums.ExportType;
import com.besafx.app.service.ProductService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
public class ReportProductController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/productStocks", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printProductStocks(
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("PRODUCTS", productService.findByParentIsNotNull(new Sort(Sort.Direction.ASC, "parent.name", "name")));

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/product/ProductStocks.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير أرصدة السلع - مراقبة المخزون", exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/productPrices", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printProductPrices(
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("PRODUCTS", productService.findByParentIsNotNull(new Sort(Sort.Direction.ASC, "parent.name", "name")));

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/product/ProductPrices.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("قائمة أسعار المنتجات", exportType, response, jasperPrint);
    }
}
