package com.besafx.app.report;

import com.besafx.app.async.TransactionalService;
import com.besafx.app.component.ReportExporter;
import com.besafx.app.enums.ExportType;
import com.besafx.app.init.Initializer;
import com.besafx.app.service.BankService;
import com.besafx.app.service.OrderSellService;
import com.besafx.app.util.CompanyOptions;
import com.besafx.app.util.JSONConverter;
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
import javax.websocket.server.PathParam;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReportBankController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportBankController.class);

    @Autowired
    private TransactionalService transactionalService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/banksData", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printBanksData(@RequestParam(value = "exportType") ExportType exportType, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("BANKS", transactionalService.readAllBanks());

        CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
        map.put("REPORT_TITLE", options.getReportTitle());
        map.put("REPORT_SUB_TITLE", options.getReportSubTitle());
        map.put("REPORT_FOOTER", options.getReportFooter());
        map.put("LOGO", options.getLogo());
        map.put("BACKGROUND", options.getBackground());

        ClassPathResource jrxmlFile = new ClassPathResource("/report/bank/BanksData.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("تقرير بالحسابات البنكية والغير بنكية", exportType, response, jasperPrint);
    }
}
