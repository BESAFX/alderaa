package com.besafx.app.report;

import com.besafx.app.component.ReportExporter;
import com.besafx.app.enums.ExportType;
import com.besafx.app.service.SupplierService;
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
public class ReportSupplierController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportSupplierController.class);

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/suppliersInfoData", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printSuppliersInfoData(
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("SUPPLIERS", supplierService.findAll(new Sort(Sort.Direction.ASC, "contact.name")));

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/supplier/SuppliersInfoData.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير بيانات الموردين", exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/suppliersBalanceData", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printSuppliersBalanceData(
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("SUPPLIERS", supplierService.findAll(new Sort(Sort.Direction.ASC, "contact.name")));

        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/supplier/SuppliersBalanceData.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير أرصدة الموردين", exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/supplierStatement/{supplierId}", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printSupplierStatement(
            @PathVariable(value = "supplierId") Long supplierId,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("LOGO", new ClassPathResource("/report/img/LOGO.png").getPath());
        map.put("VISION", new ClassPathResource("/report/img/VISION.png").getPath());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/supplier/SupplierStatement.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(Lists.newArrayList(supplierService.findOne(supplierId))));
        reportExporter.export("كشف حساب مورد", exportType, response, jasperPrint);
    }
}
