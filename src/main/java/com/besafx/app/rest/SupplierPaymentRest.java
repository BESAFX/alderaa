package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.BankTransaction;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.SupplierPayment;
import com.besafx.app.init.Initializer;
import com.besafx.app.search.SupplierPaymentSearch;
import com.besafx.app.service.BankTransactionService;
import com.besafx.app.service.SupplierPaymentService;
import com.besafx.app.util.DateConverter;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationDegree;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping(value = "/api/supplierPayment/")
public class SupplierPaymentRest {

    private final static Logger LOG = LoggerFactory.getLogger(SupplierPaymentRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "supplier[id,contact[id,shortName]]," +
            "bankTransaction[id,amount,date]," +
            "person[id,contact[id,shortName]]";

    @Autowired
    private SupplierPaymentService supplierPaymentService;

    @Autowired
    private SupplierPaymentSearch supplierPaymentSearch;

    @Autowired
    private BankTransactionService bankTransactionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_PAYMENT_CREATE')")
    @Transactional
    public String create(@RequestBody SupplierPayment supplierPayment) {
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        supplierPayment.setPerson(caller);

        if (supplierPayment.getBankTransaction().getAmount() > 0) {
            LOG.info("WITHDRAW REQUIRED AMOUNT FROM BANK ACCOUNT...");
            BankTransaction bankTransaction = supplierPayment.getBankTransaction();
            bankTransaction.setTransactionType(Initializer.transactionTypeWithdraw);
            bankTransaction.setPerson(caller);
            StringBuilder builder = new StringBuilder();
            builder.append("سحب مبلغ بقيمة ");
            builder.append(bankTransaction.getAmount());
            builder.append("ريال سعودي، ");
            builder.append(" من الحساب / ");
            builder.append(bankTransaction.getBank().getName());
            builder.append("، دفعة مالية بتاريخ ");
            builder.append(DateConverter.getDateInFormat(bankTransaction.getDate()));
            builder.append("، للمورد / ");
            builder.append(supplierPayment.getSupplier().getContact().getShortName());
            builder.append("، ");
            builder.append(supplierPayment.getBankTransaction().getNote());
            bankTransaction.setNote(builder.toString());

            supplierPayment.setBankTransaction(bankTransactionService.save(bankTransaction));
            supplierPayment = supplierPaymentService.save(supplierPayment);

            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على سداد فواتير الشراء")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.success)
                                                  .icon("add")
                                                  .build());
            entityHistoryListener.perform(builder.toString());
        }else{
            throw new CustomException("لا يمكن قبول القيمة الصفرية للسند");
        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), supplierPayment);
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_PAYMENT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        SupplierPayment supplierPayment = supplierPaymentService.findOne(id);
        if (supplierPayment != null) {
            LOG.info("DELETE BANK TRANSACTION RELATED WITH THIS PAYMENT...");
            bankTransactionService.delete(supplierPayment.getBankTransaction());
            LOG.info("DELETE THIS PAYMENT...");
            supplierPaymentService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف سند الصرف رقم / ");
            builder.append(supplierPayment.getCode());
            builder.append("، بقيمة : ");
            builder.append(supplierPayment.getBankTransaction().getAmount());
            builder.append("ريال سعودي، ");
            builder.append("، بتاريخ ");
            builder.append(DateConverter.getDateInFormat(supplierPayment.getBankTransaction().getDate()));
            builder.append("، للمورد / ");
            builder.append(supplierPayment.getSupplier().getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على سداد فواتير الشراء")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.error)
                                                  .icon("delete")
                                                  .build());
            entityHistoryListener.perform(builder.toString());
        }
    }

    @GetMapping(value = "findOne/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       supplierPaymentService.findOne(id));
    }

    @GetMapping(value = "findBySupplier/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findBySupplier(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       supplierPaymentService.findBySupplierId(id));
    }

    @GetMapping(value = "findByDateBetween/{startDate}/{endDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByDateBetween(@PathVariable(value = "startDate") Long startDate, @PathVariable(value = "endDate") Long endDate) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       supplierPaymentService.findByBankTransactionDateBetween(
                                               new DateTime(startDate).withTimeAtStartOfDay().toDate(),
                                               new DateTime(endDate).plusDays(1).withTimeAtStartOfDay().toDate()
                                                                                   ));
    }

    @GetMapping(value = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            //SupplierPayment Filters
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            //Supplier Filters
            @RequestParam(value = "supplierName", required = false) final String supplierName,
            @RequestParam(value = "supplierMobile", required = false) final String supplierMobile,
            @RequestParam(value = "filterCompareType", required = false) final String filterCompareType,
            Pageable pageable) {
        return SquigglyUtils.stringify(
                Squiggly.init(
                        new ObjectMapper(),
                        "**,".concat("content[").concat(FILTER_TABLE).concat("]")),
                supplierPaymentSearch.filter(
                        dateFrom,
                        dateTo,
                        supplierName,
                        supplierMobile,
                        filterCompareType,
                        pageable));
    }
}
