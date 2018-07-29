package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.BankTransaction;
import com.besafx.app.entity.CustomerPayment;
import com.besafx.app.entity.Person;
import com.besafx.app.init.Initializer;
import com.besafx.app.search.CustomerPaymentSearch;
import com.besafx.app.service.BankTransactionService;
import com.besafx.app.service.CustomerPaymentService;
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
@RequestMapping(value = "/api/customerPayment/")
public class CustomerPaymentRest {

    private final static Logger LOG = LoggerFactory.getLogger(CustomerPaymentRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "customer[id,contact[id,shortName]]," +
            "-bankTransaction," +
            "person[id,contact[id,shortName]]";

    @Autowired
    private CustomerPaymentService customerPaymentService;

    @Autowired
    private CustomerPaymentSearch customerPaymentSearch;

    @Autowired
    private BankTransactionService bankTransactionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_PAYMENT_CREATE')")
    @Transactional
    public String create(@RequestBody CustomerPayment customerPayment) {
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        customerPayment.setPerson(caller);

        if (customerPayment.getAmount() > 0) {
            LOG.info("WITHDRAW REQUIRED AMOUNT FROM BANK ACCOUNT...");
            BankTransaction bankTransaction = customerPayment.getBankTransaction();
            bankTransaction.setAmount(customerPayment.getAmount());
            bankTransaction.setTransactionType(Initializer.transactionTypeDeposit);
            bankTransaction.setDate(customerPayment.getDate());
            bankTransaction.setPerson(caller);
            StringBuilder builder = new StringBuilder();
            builder.append("إيداع مبلغ نقدي بقيمة ");
            builder.append(bankTransaction.getAmount());
            builder.append("ريال سعودي، ");
            builder.append(" إلى الحساب / ");
            builder.append(bankTransaction.getBank().getName());
            builder.append("، دفعة مالية بتاريخ ");
            builder.append(DateConverter.getDateInFormat(bankTransaction.getDate()));
            builder.append("، من العميل / ");
            builder.append(customerPayment.getCustomer().getContact().getShortName());
            builder.append("، ");
            builder.append(customerPayment.getNote());
            bankTransaction.setNote(builder.toString());

            customerPayment.setBankTransaction(bankTransactionService.save(bankTransaction));
            customerPayment = customerPaymentService.save(customerPayment);

            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على سداد فواتير البيع")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.success)
                                                  .icon("add")
                                                  .build());
            entityHistoryListener.perform(builder.toString());
        }else{
            throw new CustomException("لا يمكن قبول القيمة الصفرية للسند");
        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customerPayment);
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_PAYMENT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        CustomerPayment customerPayment = customerPaymentService.findOne(id);
        if (customerPayment != null) {
            LOG.info("DELETE BANK TRANSACTION RELATED WITH THIS PAYMENT...");
            bankTransactionService.delete(customerPayment.getBankTransaction());
            LOG.info("DELETE THIS PAYMENT...");
            customerPaymentService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف سند القبض رقم / ");
            builder.append(customerPayment.getCode());
            builder.append("، بقيمة : ");
            builder.append(customerPayment.getAmount());
            builder.append("ريال سعودي، ");
            builder.append("، بتاريخ ");
            builder.append(DateConverter.getDateInFormat(customerPayment.getDate()));
            builder.append("، للعميل / ");
            builder.append(customerPayment.getCustomer().getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على سداد فواتير البيع")
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
                                       customerPaymentService.findOne(id));
    }

    @GetMapping(value = "findByCustomer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByCustomer(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       customerPaymentService.findByCustomerId(id));
    }

    @GetMapping(value = "findByDateBetween/{startDate}/{endDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByDateBetween(@PathVariable(value = "startDate") Long startDate, @PathVariable(value = "endDate") Long endDate) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       customerPaymentService.findByDateBetween(
                                               new DateTime(startDate).withTimeAtStartOfDay().toDate(),
                                               new DateTime(endDate).plusDays(1).withTimeAtStartOfDay().toDate()
                                                                                   ));
    }

    @GetMapping(value = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            //CustomerPayment Filters
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            //Customer Filters
            @RequestParam(value = "customerName", required = false) final String customerName,
            @RequestParam(value = "customerMobile", required = false) final String customerMobile,
            @RequestParam(value = "filterCompareType", required = false) final String filterCompareType,
            Pageable pageable) {
        return SquigglyUtils.stringify(
                Squiggly.init(
                        new ObjectMapper(),
                        "**,".concat("content[").concat(FILTER_TABLE).concat("]")),
                customerPaymentSearch.filter(
                        dateFrom,
                        dateTo,
                        customerName,
                        customerMobile,
                        filterCompareType,
                        pageable));
    }
}
