package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Customer;
import com.besafx.app.entity.CustomerContact;
import com.besafx.app.service.ContactService;
import com.besafx.app.service.CustomerContactService;
import com.besafx.app.service.CustomerService;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationDegree;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping(value = "/api/customerContact/")
public class CustomerContactRest {

    private final static Logger LOG = LoggerFactory.getLogger(CustomerContactRest.class);

    private final String FILTER_TABLE = "" +
            "id," +
            "contact[**]," +
            "customer[id,contact[id,shortName]]";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerContactService customerContactService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_CONTACT_CREATE')")
    @Transactional
    public String create(@RequestBody CustomerContact customerContact) {
        Customer customer = customerService.findOne(customerContact.getCustomer().getId());
        if (customer == null) {
            throw new CustomException("عفواً، لا يوجد هذا العميل");
        }
        customerContact.setContact(contactService.save(customerContact.getContact()));
        customerContact = customerContactService.save(customerContact);

        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        StringBuilder builder = new StringBuilder();
        builder.append("اضافة جهة اتصال إلى  حساب العميل / ");
        builder.append("( " + customerContact.getCustomer().getContact().getName() + " )");
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

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customerContact);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_CONTACT_UPDATE')")
    @Transactional
    public String update(@RequestBody CustomerContact customerContact) {
        CustomerContact object = customerContactService.findOne(customerContact.getId());
        if (object != null) {
            customerContact.setContact(contactService.save(customerContact.getContact()));
            customerContact = customerContactService.save(customerContact);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("تعديل جهة اتصال من  حساب العميل / ");
            builder.append("( " + customerContact.getCustomer().getContact().getShortName() + " )");
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

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customerContact);
        } else {
            return null;
        }
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_CONTACT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        CustomerContact customerContact = customerContactService.findOne(id);
        if (customerContact != null) {

            LOG.info("DELETE CONTACT...");
            contactService.delete(customerContact.getContact());

            LOG.info("DELETE CUSTOMER CONTACT...");
            customerContactService.delete(customerContact);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("حذف جهة اتصال من حساب العميل / ");
            builder.append("( " + customerContact.getCustomer().getContact().getShortName() + " )");
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
                                       customerContactService.findOne(id));
    }

    @GetMapping(value = "findByCustomer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByCustomer(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       customerContactService.findByCustomerId(id));
    }
}
