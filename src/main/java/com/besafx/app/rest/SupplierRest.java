package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.config.SendSMS;
import com.besafx.app.entity.*;
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
@RequestMapping(value = "/api/supplier/")
public class SupplierRest {

    private final static Logger LOG = LoggerFactory.getLogger(SupplierRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "-supplierContacts," +
            "-supplierPayments," +
            "-billPurchases";

    private final String FILTER_DETAILS = "" +
            "**," +
            "-supplierContacts," +
            "-supplierPayments," +
            "-billPurchases";

    private final String FILTER_COMBO = "" +
            "id," +
            "code," +
            "contact[id,shortName,mobile]";

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SupplierSearch supplierSearch;

    @Autowired
    private ContactService contactService;

    @Autowired
    private SupplierContactService supplierContactService;

    @Autowired
    private BillPurchaseService billPurchaseService;

    @Autowired
    private BillPurchaseProductService billPurchaseProductService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @Autowired
    private SendSMS sendSMS;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_CREATE')")
    @Transactional
    public String create(@RequestBody Supplier supplier) {
        Supplier topSupplier = supplierService.findTopByOrderByCodeDesc();
        if (topSupplier == null) {
            supplier.setCode(1);
        } else {
            supplier.setCode(topSupplier.getCode() + 1);
        }
        supplier.setRegisterDate(new DateTime().toDate());
        supplier.setEnabled(true);
        supplier.setContact(contactService.save(supplier.getContact()));

        LOG.info("SAVE CONTACTS BEFORE SAVING SUPPLIER");
        ListIterator<SupplierContact> supplierContactListIterator = supplier.getSupplierContacts().listIterator();

        LOG.info("SAVE SUPPLIER");
        supplier = supplierService.save(supplier);

        LOG.info("CONNECT SUPPLIER WITH CONTACTS");
        while (supplierContactListIterator.hasNext()) {
            SupplierContact supplierContact = supplierContactListIterator.next();
            LOG.info("CONNECT CONTACT WITH NAME: " + supplierContact.getContact().getName());
            supplierContact.setContact(contactService.save(supplierContact.getContact()));
            supplierContact.setSupplier(supplier);
            supplierContactListIterator.set(supplierContactService.save(supplierContact));
        }

        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        StringBuilder builder = new StringBuilder();
        builder.append("إنشاء حساب المورد / ");
        builder.append("( " + supplier.getContact().getName() + " )");
        builder.append("، بواسطة ");
        builder.append(caller.getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على الموردين")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());

        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), supplier);
    }

    @PostMapping(value = "createBatch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_CREATE')")
    @Transactional
    public String createBatch(@RequestBody List<Supplier> suppliers) {
        Supplier topSupplier = supplierService.findTopByOrderByCodeDesc();
        Integer lastCode;
        if (topSupplier == null) {
            lastCode = 1;
        } else {
            lastCode = topSupplier.getCode() + 1;
        }
        ListIterator<Supplier> supplierListIterator = suppliers.listIterator();
        while (supplierListIterator.hasNext()) {
            Supplier supplier = supplierListIterator.next();
            supplier.setCode(lastCode++);
            supplier.setEnabled(true);
            supplier.setContact(contactService.save(supplier.getContact()));
        }

        LOG.info("SAVE SUPPLIERS");
        supplierService.save(suppliers);

        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        StringBuilder builder = new StringBuilder();
        builder.append("إنشاء حسابات موردين عدد / ");
        builder.append("( " + suppliers.size() + " )");
        builder.append("، بواسطة ");
        builder.append(caller.getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على الموردين")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());

        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), suppliers);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_UPDATE')")
    @Transactional
    public String update(@RequestBody Supplier supplier) {
        if (supplierService.findByCodeAndIdIsNot(supplier.getCode(), supplier.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Supplier object = supplierService.findOne(supplier.getId());
        if (object != null) {
            supplier.setContact(contactService.save(supplier.getContact()));
            supplier = supplierService.save(supplier);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("تعديل حساب المورد / ");
            builder.append("( " + supplier.getContact().getShortName() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على الموردين")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.warning)
                                                  .icon("edit")
                                                  .build());
            entityHistoryListener.perform(builder.toString());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), supplier);
        } else {
            return null;
        }
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        Supplier supplier = supplierService.findOne(id);
        if (supplier != null) {

            LOG.info("DELETE BILL PRODUCTS...");
            billPurchaseProductService.delete(supplier.getBillPurchases()
                                                      .stream()
                                                      .flatMap(contract -> contract.getBillPurchaseProducts().stream())
                                                      .collect(Collectors.toList()));

            LOG.info("DELETE BILLS...");
            billPurchaseService.delete(supplier.getBillPurchases());

            LOG.info("DELETE ALL RELATED CONTACT...");
            contactService.delete(supplier.getSupplierContacts().stream().map(SupplierContact::getContact).collect(Collectors.toList()));

            LOG.info("DELETE SUPPLIER_CONTACT...");
            supplierContactService.delete(supplier.getSupplierContacts());

            LOG.info("DELETE CONTACT...");
            contactService.delete(supplier.getContact());

            LOG.info("DELETE SUPPLIER...");
            supplierService.delete(supplier);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("حذف حساب المورد / ");
            builder.append("( " + supplier.getContact().getShortName() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على الموردين")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.error)
                                                  .icon("delete")
                                                  .build());
            entityHistoryListener.perform(builder.toString());
        }
    }

    @PostMapping(value = "sendMessage/{supplierIds}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SMS_SEND')")
    @Transactional
    public void sendMessage(@RequestBody String content, @PathVariable List<Long> supplierIds) throws Exception {
        ListIterator<Long> listIterator = supplierIds.listIterator();
        while (listIterator.hasNext()){
            Long id = listIterator.next();
            Supplier supplier = supplierService.findOne(id);
            String message = content.replaceAll("#remain#", supplier.getRemain().toString());
            Future<String> task = sendSMS.sendMessage(supplier.getContact().getMobile(), message);
            String taskResult = task.get();
            StringBuilder builder = new StringBuilder();
            builder.append("الرقم / ");
            builder.append(supplier.getContact().getMobile());
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
                                       supplierService.findOne(id));
    }

    @GetMapping(value = "findAllCombo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_COMBO),
                                       supplierService.findAll(new Sort(Sort.Direction.DESC, "contact.shortName")));
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
                supplierSearch.filter(
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
