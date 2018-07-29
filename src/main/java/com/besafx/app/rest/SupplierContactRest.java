package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.config.SendSMS;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Supplier;
import com.besafx.app.entity.SupplierContact;
import com.besafx.app.search.SupplierSearch;
import com.besafx.app.service.*;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/supplierContact/")
public class SupplierContactRest {

    private final static Logger LOG = LoggerFactory.getLogger(SupplierContactRest.class);

    private final String FILTER_TABLE = "" +
            "id," +
            "contact[**]," +
            "supplier[id,contact[id,shortName]]";

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SupplierContactService supplierContactService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_CONTACT_CREATE')")
    @Transactional
    public String create(@RequestBody SupplierContact supplierContact) {
        Supplier supplier = supplierService.findOne(supplierContact.getSupplier().getId());
        if (supplier == null) {
            throw new CustomException("عفواً، لا يوجد هذا المورد");
        }
        supplierContact.setContact(contactService.save(supplierContact.getContact()));
        supplierContact = supplierContactService.save(supplierContact);

        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        StringBuilder builder = new StringBuilder();
        builder.append("اضافة جهة اتصال إلى  حساب المورد / ");
        builder.append("( " + supplierContact.getSupplier().getContact().getName() + " )");
        builder.append("، بواسطة ");
        builder.append(caller.getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على جهات الاتصال")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());

        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), supplierContact);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_CONTACT_UPDATE')")
    @Transactional
    public String update(@RequestBody SupplierContact supplierContact) {
        SupplierContact object = supplierContactService.findOne(supplierContact.getId());
        if (object != null) {
            supplierContact.setContact(contactService.save(supplierContact.getContact()));
            supplierContact = supplierContactService.save(supplierContact);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("تعديل جهة اتصال من  حساب المورد / ");
            builder.append("( " + supplierContact.getSupplier().getContact().getShortName() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على جهات الاتصال")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.warning)
                                                  .icon("edit")
                                                  .build());
            entityHistoryListener.perform(builder.toString());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), supplierContact);
        } else {
            return null;
        }
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_CONTACT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        SupplierContact supplierContact = supplierContactService.findOne(id);
        if (supplierContact != null) {

            LOG.info("DELETE CONTACT...");
            contactService.delete(supplierContact.getContact());

            LOG.info("DELETE SUPPLIER CONTACT...");
            supplierContactService.delete(supplierContact);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("حذف جهة اتصال من حساب المورد / ");
            builder.append("( " + supplierContact.getSupplier().getContact().getShortName() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على جهات الاتصال")
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
                                       supplierContactService.findOne(id));
    }

    @GetMapping(value = "findBySupplier/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findBySupplier(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       supplierContactService.findBySupplierId(id));
    }
}
