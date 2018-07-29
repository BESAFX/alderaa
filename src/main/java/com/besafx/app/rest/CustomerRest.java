package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.config.SendSMS;
import com.besafx.app.entity.Customer;
import com.besafx.app.entity.CustomerContact;
import com.besafx.app.entity.Person;
import com.besafx.app.search.CustomerSearch;
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
@RequestMapping(value = "/api/customer/")
public class CustomerRest {

    private final static Logger LOG = LoggerFactory.getLogger(CustomerRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "-customerContacts," +
            "-customerPayments," +
            "-billSells";

    private final String FILTER_DETAILS = "" +
            "**," +
            "-customerContacts," +
            "-customerPayments," +
            "-billSells";

    private final String FILTER_COMBO = "" +
            "id," +
            "code," +
            "contact[id,shortName,mobile]";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerSearch customerSearch;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CustomerContactService customerContactService;

    @Autowired
    private BillSellService billSellService;

    @Autowired
    private BillSellProductService billSellProductService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @Autowired
    private SendSMS sendSMS;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_CREATE')")
    @Transactional
    public String create(@RequestBody Customer customer) {
        Customer topCustomer = customerService.findTopByOrderByCodeDesc();
        if (topCustomer == null) {
            customer.setCode(1);
        } else {
            customer.setCode(topCustomer.getCode() + 1);
        }
        customer.setRegisterDate(new DateTime().toDate());
        customer.setEnabled(true);
        customer.setContact(contactService.save(customer.getContact()));

        LOG.info("SAVE CONTACTS BEFORE SAVING CUSTOMER");
        ListIterator<CustomerContact> customerContactListIterator = customer.getCustomerContacts().listIterator();

        LOG.info("SAVE CUSTOMER");
        customer = customerService.save(customer);

        LOG.info("CONNECT CUSTOMER WITH CONTACTS");
        while (customerContactListIterator.hasNext()) {
            CustomerContact customerContact = customerContactListIterator.next();
            LOG.info("CONNECT CONTACT WITH NAME: " + customerContact.getContact().getName());
            customerContact.setContact(contactService.save(customerContact.getContact()));
            customerContact.setCustomer(customer);
            customerContactListIterator.set(customerContactService.save(customerContact));
        }

        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        StringBuilder builder = new StringBuilder();
        builder.append("إنشاء حساب العميل / ");
        builder.append("( " + customer.getContact().getName() + " )");
        builder.append("، بواسطة ");
        builder.append(caller.getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على العملاء")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customer);
    }

    @PostMapping(value = "createBatch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_CREATE')")
    @Transactional
    public String createBatch(@RequestBody List<Customer> customers) {
        Customer topCustomer = customerService.findTopByOrderByCodeDesc();
        Integer lastCode;
        if (topCustomer == null) {
            lastCode = 1;
        } else {
            lastCode = topCustomer.getCode() + 1;
        }
        ListIterator<Customer> customerListIterator = customers.listIterator();
        while (customerListIterator.hasNext()) {
            Customer customer = customerListIterator.next();
            customer.setCode(lastCode++);
            customer.setEnabled(true);
            customer.setContact(contactService.save(customer.getContact()));
        }

        LOG.info("SAVE CUSTOMERS");
        customerService.save(customers);

        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        StringBuilder builder = new StringBuilder();
        builder.append("إنشاء حسابات عملاء عدد / ");
        builder.append("( " + customers.size() + " )");
        builder.append("، بواسطة ");
        builder.append(caller.getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على العملاء")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customers);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_UPDATE')")
    @Transactional
    public String update(@RequestBody Customer customer) {
        if (customerService.findByCodeAndIdIsNot(customer.getCode(), customer.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Customer object = customerService.findOne(customer.getId());
        if (object != null) {
            customer.setContact(contactService.save(customer.getContact()));
            customer = customerService.save(customer);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("تعديل حساب العميل / ");
            builder.append("( " + customer.getContact().getShortName() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على العملاء")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.warning)
                                                  .icon("edit")
                                                  .build());
            entityHistoryListener.perform(builder.toString());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customer);
        } else {
            return null;
        }
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        Customer customer = customerService.findOne(id);
        if (customer != null) {

            LOG.info("DELETE BILL PRODUCTS...");
            billSellProductService.delete(customer.getBillSells()
                                                  .stream()
                                                  .flatMap(contract -> contract.getBillSellProducts().stream())
                                                  .collect(Collectors.toList()));

            LOG.info("DELETE BILLS...");
            billSellService.delete(customer.getBillSells());

            LOG.info("DELETE ALL RELATED CONTACT...");
            contactService.delete(customer.getCustomerContacts().stream().map(CustomerContact::getContact).collect(Collectors.toList()));

            LOG.info("DELETE CUSTOMER_CONTACT...");
            customerContactService.delete(customer.getCustomerContacts());

            LOG.info("DELETE CONTACT...");
            contactService.delete(customer.getContact());

            LOG.info("DELETE CUSTOMER...");
            customerService.delete(customer);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("حذف حساب العميل / ");
            builder.append("( " + customer.getContact().getShortName() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على العملاء")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.error)
                                                  .icon("delete")
                                                  .build());
            entityHistoryListener.perform(builder.toString());
        }
    }

    @PostMapping(value = "sendMessage/{customerIds}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SMS_SEND')")
    @Transactional
    public void sendMessage(@RequestBody String content, @PathVariable List<Long> customerIds) throws Exception {
        ListIterator<Long> listIterator = customerIds.listIterator();
        while (listIterator.hasNext()) {
            Long id = listIterator.next();
            Customer customer = customerService.findOne(id);
            String message = content.replaceAll("#remain#", customer.getRemain().toString());
            Future<String> task = sendSMS.sendMessage(customer.getContact().getMobile(), message);
            String taskResult = task.get();
            StringBuilder builder = new StringBuilder();
            builder.append("الرقم / ");
            builder.append(customer.getContact().getMobile());
            builder.append("<br/>");
            builder.append(" محتوى الرسالة : ");
            builder.append(message);
            builder.append("<br/>");
            builder.append(" ، نتيجة الإرسال: ");
            builder.append(taskResult);
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.information).build());
        }
    }

    @GetMapping(value = "findOne/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETAILS),
                                       customerService.findOne(id));
    }

    @GetMapping(value = "findAllCombo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_COMBO),
                                       customerService.findAll(new Sort(Sort.Direction.DESC, "contact.shortName")));
    }

    @GetMapping(value = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Integer codeFrom,
            @RequestParam(value = "codeTo", required = false) final Integer codeTo,
            @RequestParam(value = "registerDateFrom", required = false) final Long registerDateFrom,
            @RequestParam(value = "registerDateTo", required = false) final Long registerDateTo,
            @RequestParam(value = "name", required = false) final String name,
            @RequestParam(value = "mobile", required = false) final String mobile,
            @RequestParam(value = "phone", required = false) final String phone,
            @RequestParam(value = "nationality", required = false) final String nationality,
            @RequestParam(value = "identityNumber", required = false) final String identityNumber,
            @RequestParam(value = "qualification", required = false) final String qualification,
            @RequestParam(value = "filterCompareType", required = false) final String filterCompareType,
            Pageable pageable) {
        return SquigglyUtils.stringify(
                Squiggly.init(
                        new ObjectMapper(),
                        "**,".concat("content[").concat(FILTER_TABLE).concat("]")),
                customerSearch.filter(
                        codeFrom,
                        codeTo,
                        registerDateFrom,
                        registerDateTo,
                        name,
                        mobile,
                        phone,
                        nationality,
                        identityNumber,
                        qualification,
                        filterCompareType,
                        pageable));
    }
}
