package com.besafx.app.report;

import com.besafx.app.component.ReportExporter;
import com.besafx.app.entity.BankTransaction;
import com.besafx.app.entity.CustomerPayment;
import com.besafx.app.entity.SupplierPayment;
import com.besafx.app.enums.ExportType;
import com.besafx.app.service.BankTransactionService;
import com.besafx.app.service.CustomerPaymentService;
import com.besafx.app.service.SupplierPaymentService;
import com.besafx.app.util.ArabicLiteralNumberParser;
import com.besafx.app.util.WrapperUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ReportReceiptController {

    private final static Logger LOG = LoggerFactory.getLogger(ReportReceiptController.class);

    @Autowired
    private BankTransactionService bankTransactionService;

    @Autowired
    private CustomerPaymentService customerPaymentService;

    @Autowired
    private SupplierPaymentService supplierPaymentService;

    @Autowired
    private ReportExporter reportExporter;

    @GetMapping(value = "/report/customerPayment/{customerPaymentId}", produces = "application/pdf")
    @ResponseBody
    public void printCustomerPayment(
            @PathVariable(value = "customerPaymentId") Long customerPaymentId,
            HttpServletResponse response) throws Exception {
        CustomerPayment customerPayment = customerPaymentService.findOne(customerPaymentId);
        Map<String, Object> map = new HashMap<>();
        map.put("RECEIPT_TYPE", "سند قبض");
        map.put("RECEIPT_DATE", customerPayment.getBankTransaction().getDate());
        map.put("RECEIPT_AMOUNT", customerPayment.getBankTransaction().getAmount());
        map.put("RECEIPT_CODE", customerPayment.getCode());
        map.put("RECEIPT_OTHER_NAME", customerPayment.getPerson().getContact().getShortName());

        List<WrapperUtil> wrapperUtils = new ArrayList<>();
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("استلمنا من المكرم: " + customerPayment.getCustomer().getContact().getShortName());
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("مبلغ وقدره: " + ArabicLiteralNumberParser.literalValueOf(customerPayment.getBankTransaction().getAmount()) + " ريال سعودي فقط لا غير");
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            switch (customerPayment.getBankTransaction().getPaymentMethod()) {
                case Cash:
                    wrapperUtil.setObj1("نقداً،،،");
                    break;
                case Check:
                    wrapperUtil.setObj1("شيك رقم: " + customerPayment.getBankTransaction().getCheckOrVisaNumber());
                    break;
                case Visa:
                    wrapperUtil.setObj1("شبكة رقم: " + customerPayment.getBankTransaction().getCheckOrVisaNumber());
                    break;
                default:
                    wrapperUtil.setObj1("");
                    break;
            }
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("الإستبيان: " + customerPayment.getBankTransaction().getNote());
            wrapperUtils.add(wrapperUtil);
        }

        map.put("CONTENTS", wrapperUtils);

        ClassPathResource jrxmlFile = new ClassPathResource("/report/receipt/Receipt.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        List<JasperPrint> jasperPrints = new ArrayList<>();
        jasperPrints.add(JasperFillManager.fillReport(jasperReport, map));
        reportExporter.export(ExportType.PDF, response, jasperPrints);
    }

    @GetMapping(value = "/report/supplierPayment/{supplierPaymentId}", produces = "application/pdf")
    @ResponseBody
    public void printSupplierPayment(
            @PathVariable(value = "supplierPaymentId") Long supplierPaymentId,
            HttpServletResponse response) throws Exception {
        SupplierPayment supplierPayment = supplierPaymentService.findOne(supplierPaymentId);
        Map<String, Object> map = new HashMap<>();
        map.put("RECEIPT_TYPE", "سند صرف");
        map.put("RECEIPT_DATE", supplierPayment.getBankTransaction().getDate());
        map.put("RECEIPT_AMOUNT", supplierPayment.getBankTransaction().getAmount());
        map.put("RECEIPT_CODE", supplierPayment.getCode());
        map.put("RECEIPT_OTHER_NAME", supplierPayment.getPerson().getContact().getShortName());

        List<WrapperUtil> wrapperUtils = new ArrayList<>();
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("صرفنا إلى المكرم: " + supplierPayment.getSupplier().getContact().getShortName());
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("مبلغ وقدره: " + ArabicLiteralNumberParser.literalValueOf(supplierPayment.getBankTransaction().getAmount()) + " ريال سعودي فقط لا غير");
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            switch (supplierPayment.getBankTransaction().getPaymentMethod()) {
                case Cash:
                    wrapperUtil.setObj1("نقداً،،،");
                    break;
                case Check:
                    wrapperUtil.setObj1("شيك رقم: " + supplierPayment.getBankTransaction().getCheckOrVisaNumber());
                    break;
                case Visa:
                    wrapperUtil.setObj1("شبكة رقم: " + supplierPayment.getBankTransaction().getCheckOrVisaNumber());
                    break;
                default:
                    wrapperUtil.setObj1("");
                    break;
            }
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("الإستبيان: " + supplierPayment.getBankTransaction().getNote());
            wrapperUtils.add(wrapperUtil);
        }

        map.put("CONTENTS", wrapperUtils);

        ClassPathResource jrxmlFile = new ClassPathResource("/report/receipt/Receipt.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        List<JasperPrint> jasperPrints = new ArrayList<>();
        jasperPrints.add(JasperFillManager.fillReport(jasperReport, map));
        reportExporter.export(ExportType.PDF, response, jasperPrints);
    }

    @RequestMapping(value = "/report/receiptDeposit/{bankTransactionId}", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printReceiptDeposit(
            @PathVariable(value = "bankTransactionId") Long bankTransactionId,
            HttpServletResponse response) throws Exception {
        BankTransaction bankTransaction = bankTransactionService.findOne(bankTransactionId);
        Map<String, Object> map = new HashMap<>();
        map.put("RECEIPT_TYPE", "سنـــد قبض");
        map.put("RECEIPT_DATE", bankTransaction.getDate());
        map.put("RECEIPT_AMOUNT", bankTransaction.getAmount());
        map.put("RECEIPT_CODE", bankTransaction.getCode().intValue());
        map.put("RECEIPT_OTHER_NAME", bankTransaction.getPerson().getContact().getShortName());

        List<WrapperUtil> wrapperUtils = new ArrayList<>();
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("استلمنا من المكرم: " + bankTransaction.getOtherName());
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("مبلغ وقدره: " + ArabicLiteralNumberParser.literalValueOf(bankTransaction.getAmount()) + " ريال سعودي فقط لا غير");
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            switch (bankTransaction.getPaymentMethod()) {
                case Cash:
                    wrapperUtil.setObj1("نقداً،،،");
                    break;
                case Check:
                    wrapperUtil.setObj1("شيك رقم: " + bankTransaction.getCheckOrVisaNumber());
                    break;
                case Visa:
                    wrapperUtil.setObj1("شبكة رقم: " + bankTransaction.getCheckOrVisaNumber());
                    break;
                default:
                    wrapperUtil.setObj1("");
                    break;
            }
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("الإستبيان: " + bankTransaction.getNote());
            wrapperUtils.add(wrapperUtil);
        }

        map.put("CONTENTS", wrapperUtils);

        ClassPathResource jrxmlFile = new ClassPathResource("/report/receipt/Receipt.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        List<JasperPrint> jasperPrints = new ArrayList<>();
        jasperPrints.add(JasperFillManager.fillReport(jasperReport, map));
        reportExporter.export(ExportType.PDF, response, jasperPrints);
    }

    @RequestMapping(value = "/report/receiptWithdraw/{bankTransactionId}", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printReceiptWithdraw(
            @PathVariable(value = "bankTransactionId") Long bankTransactionId,
            HttpServletResponse response) throws Exception {
        BankTransaction bankTransaction = bankTransactionService.findOne(bankTransactionId);
        Map<String, Object> map = new HashMap<>();
        map.put("RECEIPT_TYPE", "سنـــد صرف");
        map.put("RECEIPT_DATE", bankTransaction.getDate());
        map.put("RECEIPT_AMOUNT", bankTransaction.getAmount());
        map.put("RECEIPT_CODE", bankTransaction.getCode().intValue());
        map.put("RECEIPT_OTHER_NAME", bankTransaction.getOtherName());

        List<WrapperUtil> wrapperUtils = new ArrayList<>();
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("صرفنا إلى المكرم: " + bankTransaction.getOtherName());
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("مبلغ وقدره: " + ArabicLiteralNumberParser.literalValueOf(bankTransaction.getAmount()) + " ريال سعودي فقط لا غير");
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            switch (bankTransaction.getPaymentMethod()) {
                case Cash:
                    wrapperUtil.setObj1("نقداً،،،");
                    break;
                case Check:
                    wrapperUtil.setObj1("شيك رقم: " + bankTransaction.getCheckOrVisaNumber());
                    break;
                case Visa:
                    wrapperUtil.setObj1("شبكة رقم: " + bankTransaction.getCheckOrVisaNumber());
                    break;
                default:
                    wrapperUtil.setObj1("");
                    break;
            }
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("الإستبيان: " + bankTransaction.getNote());
            wrapperUtils.add(wrapperUtil);
        }

        map.put("CONTENTS", wrapperUtils);

        ClassPathResource jrxmlFile = new ClassPathResource("/report/receipt/Receipt.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        List<JasperPrint> jasperPrints = new ArrayList<>();
        jasperPrints.add(JasperFillManager.fillReport(jasperReport, map));
        reportExporter.export(ExportType.PDF, response, jasperPrints);
    }

    @RequestMapping(value = "/report/receiptExpense/{bankTransactionId}", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void printReceiptExpense(
            @PathVariable(value = "bankTransactionId") Long bankTransactionId,
            HttpServletResponse response) throws Exception {
        BankTransaction bankTransaction = bankTransactionService.findOne(bankTransactionId);
        Map<String, Object> map = new HashMap<>();
        map.put("RECEIPT_TYPE", "سنـــد صرف");
        map.put("RECEIPT_DATE", bankTransaction.getDate());
        map.put("RECEIPT_AMOUNT", bankTransaction.getAmount());
        map.put("RECEIPT_CODE", bankTransaction.getCode().intValue());
        map.put("RECEIPT_OTHER_NAME", bankTransaction.getOtherName());

        List<WrapperUtil> wrapperUtils = new ArrayList<>();
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("صرفنا إلى المكرم: " + bankTransaction.getOtherName());
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("مبلغ وقدره: " + ArabicLiteralNumberParser.literalValueOf(bankTransaction.getAmount()) + " ريال سعودي فقط لا غير");
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            switch (bankTransaction.getPaymentMethod()) {
                case Cash:
                    wrapperUtil.setObj1("نقداً،،،");
                    break;
                case Check:
                    wrapperUtil.setObj1("شيك رقم: " + bankTransaction.getCheckOrVisaNumber());
                    break;
                case Visa:
                    wrapperUtil.setObj1("شبكة رقم: " + bankTransaction.getCheckOrVisaNumber());
                    break;
                default:
                    wrapperUtil.setObj1("");
                    break;
            }
            wrapperUtils.add(wrapperUtil);
        }
        {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1("الإستبيان: " + bankTransaction.getNote());
            wrapperUtils.add(wrapperUtil);
        }

        map.put("CONTENTS", wrapperUtils);

        ClassPathResource jrxmlFile = new ClassPathResource("/report/receipt/Receipt.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        List<JasperPrint> jasperPrints = new ArrayList<>();
        jasperPrints.add(JasperFillManager.fillReport(jasperReport, map));
        reportExporter.export(ExportType.PDF, response, jasperPrints);
    }

}
