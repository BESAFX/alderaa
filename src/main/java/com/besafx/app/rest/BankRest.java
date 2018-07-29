package com.besafx.app.rest;

import com.besafx.app.async.TransactionalService;
import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.entity.Bank;
import com.besafx.app.entity.BankTransaction;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.projection.BankTransactionAmount;
import com.besafx.app.init.Initializer;
import com.besafx.app.service.BankService;
import com.besafx.app.service.BankTransactionService;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationDegree;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.ListIterator;

@RestController
@RequestMapping(value = "/api/bank/")
public class BankRest {

    private final static Logger LOG = LoggerFactory.getLogger(BankRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "person[id,contact[id,shortName]]," +
            "-bankTransactions";

    @Autowired
    private BankService bankService;

    @Autowired
    private BankTransactionService bankTransactionService;

    @Autowired
    private TransactionalService transactionalService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BANK_CREATE')")
    @Transactional
    public String create(@RequestBody Bank bank) {
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        bank.setRegisterDate(new DateTime().toDate());
        bank.setPerson(caller);
        bank = bankService.save(bank);

        StringBuilder builder = new StringBuilder();
        builder.append("إنشاء الحساب / ");
        builder.append("( " + bank.getName() + " )");
        builder.append("، بواسطة ");
        builder.append(caller.getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على الحسابات")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());

        entityHistoryListener.perform(builder.toString());

        if (bank.getOpenCash() > 0) {
            LOG.info("CREATE OPEN CASH WITH: " + bank.getOpenCash() + " SAR");
            BankTransaction bankTransaction = new BankTransaction();
            bankTransaction.setAmount(bank.getOpenCash());
            bankTransaction.setDate(new DateTime().toDate());
            bankTransaction.setBank(bank);
            bankTransaction.setTransactionType(Initializer.transactionTypeDeposit);
            bankTransaction.setPerson(caller);
            builder = new StringBuilder();
            builder.append("إيداع مبلغ نقدي بقيمة ");
            builder.append(bank.getOpenCash());
            builder.append("ريال سعودي، ");
            builder.append(" للحساب / ");
            builder.append(bank.getName());
            builder.append("، كرصيد افتتاحي");
            bankTransaction.setNote(builder.toString());
            bankTransactionService.save(bankTransaction);

            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على الإيداعات")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.success)
                                                  .icon("add")
                                                  .build());

            entityHistoryListener.perform(builder.toString());
        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), bank);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BANK_UPDATE')")
    @Transactional
    public String update(@RequestBody Bank bank) {
        Bank object = bankService.findOne(bank.getId());
        if (object != null) {
            bank = bankService.save(bank);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("تعديل الحساب / ");
            builder.append("( " + bank.getName() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على الحسابات")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.warning)
                                                  .icon("edit")
                                                  .build());

            entityHistoryListener.perform(builder.toString());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), bank);
        } else {
            return null;
        }
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BANK_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        Bank bank = bankService.findOne(id);
        if (bank != null) {
            LOG.info("DELETE ALL BANK TRANSACTIONS");
            bankTransactionService.delete(bank.getBankTransactions());
            LOG.info("DELETE BANK");
            bankService.delete(bank);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("حذف الحساب / ");
            builder.append("( " + bank.getName() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على الحسابات")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.error)
                                                  .icon("delete")
                                                  .build());

            entityHistoryListener.perform(builder.toString());
        }
    }

    @GetMapping(value = "findAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionalService.readAllBanks());
    }
}
