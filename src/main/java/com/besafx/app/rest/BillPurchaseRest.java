package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.*;
import com.besafx.app.entity.enums.OfferCondition;
import com.besafx.app.entity.enums.OrderPurchaseCondition;
import com.besafx.app.search.BillPurchaseSearch;
import com.besafx.app.service.BillPurchaseProductService;
import com.besafx.app.service.BillPurchaseService;
import com.besafx.app.service.OrderPurchaseService;
import com.besafx.app.service.SupplierService;
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
import java.util.ListIterator;

@RestController
@RequestMapping(value = "/api/billPurchase/")
public class BillPurchaseRest {

    private final static Logger LOG = LoggerFactory.getLogger(BillPurchaseRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "supplier[id,contact[id,mobile,shortName]]," +
            "-billPurchaseProducts," +
            "-billPurchaseAttaches," +
            "person[id,contact[id,shortName]]";

    private final String FILTER_DETAILS = "" +
            "**," +
            "supplier[id,contact[id,mobile,shortName]]," +
            "billPurchaseProducts[**,-billPurchase,product[id,name]]," +
            "billPurchaseAttaches[**,-billPurchase,attach[**,person[id,contact[shortName]]]]," +
            "person[id,contact[id,shortName]]";

    @Autowired
    private BillPurchaseService billPurchaseService;

    @Autowired
    private BillPurchaseProductService billPurchaseProductService;

    @Autowired
    private BillPurchaseSearch billPurchaseSearch;

    @Autowired
    private OrderPurchaseService orderPurchaseService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_PURCHASE_CREATE')")
    @Transactional
    public String create(@RequestBody BillPurchase billPurchase) {
        BillPurchase tempBillPurchase = billPurchaseService.findByCode(billPurchase.getCode());
        if (tempBillPurchase != null) {
            throw new CustomException("عفواً، رقم الفاتورة المدخل غير متاح، حاول برقم آخر");
        }
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        billPurchase.setWrittenDate(new DateTime().toDate());
        billPurchase.setPerson(caller);
        billPurchase = billPurchaseService.save(billPurchase);

        LOG.info("ربط الأصناف المطلوبة مع الفاتورة");
        ListIterator<BillPurchaseProduct> billPurchaseProductListIterator = billPurchase.getBillPurchaseProducts().listIterator();
        while (billPurchaseProductListIterator.hasNext()) {
            BillPurchaseProduct billPurchaseProduct = billPurchaseProductListIterator.next();
            billPurchaseProduct.setDate(new DateTime().toDate());
            billPurchaseProduct.setBillPurchase(billPurchase);
            billPurchaseProductListIterator.set(billPurchaseProductService.save(billPurchaseProduct));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("انشاء فاتورة شراء رقم / ");
        builder.append(billPurchase.getCode());
        builder.append("، بمجموع أسعار = ");
        builder.append(billPurchase.getTotalPrice());
        builder.append("، وخصم بمقدار ");
        builder.append(billPurchase.getDiscount());
        builder.append("، وقيمة مضافة بمقدار ");
        builder.append(billPurchase.getTotalVat());
        builder.append("، وأصناف عدد " + billPurchase.getBillPurchaseProducts().size() + " صنف");
        builder.append("، للمورد / ");
        builder.append(billPurchase.getSupplier().getContact().getShortName());
        builder.append("، ");
        builder.append(billPurchase.getNote());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير الشراء")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billPurchase);
    }

    @PostMapping(value = "createFromOrder", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_PURCHASE_CREATE')")
    @Transactional
    public String createFromOrder(@RequestBody OrderPurchase orderPurchase) {
        orderPurchase = orderPurchaseService.findOne(orderPurchase.getId());
        BillPurchase billPurchase = new BillPurchase();
        BillPurchase topBillPurchase = billPurchaseService.findTopByOrderByCodeDesc();
        if (topBillPurchase == null) {
            billPurchase.setCode(Long.valueOf(1));
        }else{
            billPurchase.setCode(topBillPurchase.getCode() + 1);
        }
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        billPurchase.setSupplier(orderPurchase.getSupplier());
        billPurchase.setDiscount(orderPurchase.getDiscount());
        billPurchase.setTransferFees(orderPurchase.getTransferFees());
        billPurchase.setNote(orderPurchase.getNote());
        billPurchase.setWrittenDate(new DateTime().toDate());
        billPurchase.setPerson(caller);
        billPurchase = billPurchaseService.save(billPurchase);

        LOG.info("CONNECT ORDER PRODUCTS WITH BILL PURCHASE");
        ListIterator<OrderPurchaseProduct> orderPurchaseProductListIterator = orderPurchase.getOrderPurchaseProducts().listIterator();
        while (orderPurchaseProductListIterator.hasNext()) {
            OrderPurchaseProduct orderPurchaseProduct = orderPurchaseProductListIterator.next();
            BillPurchaseProduct billPurchaseProduct = new BillPurchaseProduct();
            billPurchaseProduct.setBillPurchase(billPurchase);
            billPurchaseProduct.setProduct(orderPurchaseProduct.getProduct());
            billPurchaseProduct.setQuantity(orderPurchaseProduct.getQuantity());
            billPurchaseProduct.setUnitPurchasePrice(orderPurchaseProduct.getUnitPurchasePrice());
            billPurchaseProduct.setUnitVat(orderPurchaseProduct.getUnitVat());
            //TO-DO
            billPurchaseProduct.setUnitSellPrice(orderPurchaseProduct.getUnitPurchasePrice());
            billPurchaseProduct.setDate(new DateTime().toDate());
            billPurchase.getBillPurchaseProducts().add(billPurchaseProductService.save(billPurchaseProduct));
        }

        LOG.info("CHANGE ORDER STATE TO BILLED");
        orderPurchase.setCondition(OrderPurchaseCondition.Billed);
        orderPurchaseService.save(orderPurchase);
        StringBuilder builder = new StringBuilder();
        builder.append("تمت الموافقة على أمر الشراء رقم ");
        builder.append(" ( " + orderPurchase.getCode() + " ) ");
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير الشراء")
                                              .message(builder.toString())
                                              .type(NotificationDegree.information)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        builder = new StringBuilder();
        builder.append("تحويل أمر الشراء رقم / ");
        builder.append(orderPurchase.getCode());
        builder.append("إلى فاتورة شراء رقم / ");
        builder.append(billPurchase.getCode());
        builder.append("، بمجموع أسعار = ");
        builder.append(billPurchase.getTotalPrice());
        builder.append("، وخصم بمقدار ");
        builder.append(billPurchase.getDiscount());
        builder.append("، وقيمة مضافة بمقدار ");
        builder.append(billPurchase.getTotalVat());
        builder.append("، وأصناف عدد " + billPurchase.getBillPurchaseProducts().size() + " صنف");
        builder.append("، من المورد / ");
        builder.append(billPurchase.getSupplier().getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير الشراء")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billPurchase);
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_PURCHASE_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        BillPurchase billPurchase = billPurchaseService.findOne(id);
        if (billPurchase != null) {
            LOG.info("DELETE ALL PRODUCTS RELATED WITH THIS BILL...");
            billPurchaseProductService.delete(billPurchase.getBillPurchaseProducts());
            LOG.info("DELETE THIS BILL...");
            billPurchaseService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف الفاتورة رقم / ");
            builder.append(billPurchase.getCode());
            builder.append("، بقيمة : ");
            builder.append(billPurchase.getTotalPrice());
            builder.append("ريال سعودي، ");
            builder.append("، بتاريخ ");
            builder.append(DateConverter.getDateInFormat(billPurchase.getWrittenDate()));
            builder.append("للمورد / ");
            builder.append(billPurchase.getSupplier().getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على فواتير الشراء")
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
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETAILS),
                                       billPurchaseService.findOne(id));
    }

    @GetMapping(value = "findBySupplier/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findBySupplier(@PathVariable(value = "supplierId") Long supplierId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETAILS),
                                       billPurchaseService.findBySupplier(supplierService.findOne(supplierId)));
    }

    @GetMapping(value = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            //BillPurchase Filters
            @RequestParam(value = "codeFrom", required = false) final Integer codeFrom,
            @RequestParam(value = "codeTo", required = false) final Integer codeTo,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            //Supplier Filters
            @RequestParam(value = "supplierCodeFrom", required = false) final Integer supplierCodeFrom,
            @RequestParam(value = "supplierCodeTo", required = false) final Integer supplierCodeTo,
            @RequestParam(value = "supplierRegisterDateFrom", required = false) final Long supplierRegisterDateFrom,
            @RequestParam(value = "supplierRegisterDateTo", required = false) final Long supplierRegisterDateTo,
            @RequestParam(value = "supplierName", required = false) final String supplierName,
            @RequestParam(value = "supplierMobile", required = false) final String supplierMobile,
            @RequestParam(value = "filterCompareType", required = false) final String filterCompareType,
            Pageable pageable) {
        return SquigglyUtils.stringify(
                Squiggly.init(
                        new ObjectMapper(),
                        "**,".concat("content[").concat(FILTER_TABLE).concat("]")),
                billPurchaseSearch.filter(
                        codeFrom,
                        codeTo,
                        dateFrom,
                        dateTo,
                        supplierCodeFrom,
                        supplierCodeTo,
                        supplierRegisterDateFrom,
                        supplierRegisterDateTo,
                        supplierName,
                        supplierMobile,
                        filterCompareType,
                        pageable));
    }
}
