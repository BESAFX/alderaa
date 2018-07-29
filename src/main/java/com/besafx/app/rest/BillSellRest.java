package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.entity.*;
import com.besafx.app.entity.enums.OfferCondition;
import com.besafx.app.entity.enums.OrderSellCondition;
import com.besafx.app.search.BillSellSearch;
import com.besafx.app.service.*;
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
@RequestMapping(value = "/api/billSell/")
public class BillSellRest {

    private final static Logger LOG = LoggerFactory.getLogger(BillSellRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "customer[id,contact[id,mobile,shortName]]," +
            "-billSellProducts," +
            "-billSellAttaches," +
            "person[id,contact[id,shortName]]";

    private final String FILTER_DETAILS = "" +
            "**," +
            "customer[id,contact[id,mobile,shortName]]," +
            "billSellProducts[**,-billSell,product[id,name]]," +
            "billSellAttaches[**,-billSell,attach[**,person[id,contact[shortName]]]]," +
            "person[id,contact[id,shortName]]";

    @Autowired
    private BillSellService billSellService;

    @Autowired
    private BillSellProductService billSellProductService;

    @Autowired
    private BillSellSearch billSellSearch;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private OrderSellService orderSellService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_CREATE')")
    @Transactional
    public String create(@RequestBody BillSell billSell) {
        BillSell topBillSell = billSellService.findTopByOrderByCodeDesc();
        if (topBillSell == null) {
            billSell.setCode(Long.valueOf(1));
        } else {
            billSell.setCode(topBillSell.getCode() + 1);
        }

        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        billSell.setWrittenDate(new DateTime().toDate());
        billSell.setPerson(caller);
        billSell = billSellService.save(billSell);

        LOG.info("ربط الأصناف المطلوبة مع الفاتورة");
        ListIterator<BillSellProduct> billSellProductListIterator = billSell.getBillSellProducts().listIterator();
        while (billSellProductListIterator.hasNext()) {
            BillSellProduct billSellProduct = billSellProductListIterator.next();
            billSellProduct.setDate(new DateTime().toDate());
            billSellProduct.setBillSell(billSell);
            billSellProductListIterator.set(billSellProductService.save(billSellProduct));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("انشاء فاتورة بيع رقم / ");
        builder.append(billSell.getCode());
        builder.append("، بمجموع أسعار = ");
        builder.append(billSell.getTotalPrice());
        builder.append("، وخصم بمقدار ");
        builder.append(billSell.getDiscount());
        builder.append("، وقيمة مضافة بمقدار ");
        builder.append(billSell.getTotalVat());
        builder.append("، وأصناف عدد " + billSell.getBillSellProducts().size() + " صنف");
        builder.append("، للعميل / ");
        builder.append(billSell.getCustomer().getContact().getShortName());
        builder.append("، ");
        builder.append(billSell.getNote());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير البيع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSell);
    }

    @PostMapping(value = "createFromOffer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_CREATE')")
    @Transactional
    public String createFromOffer(@RequestBody Offer offer) {
        offer = offerService.findOne(offer.getId());
        BillSell billSell = new BillSell();
        BillSell topBillSell = billSellService.findTopByOrderByCodeDesc();
        if (topBillSell == null) {
            billSell.setCode(Long.valueOf(1));
        }else{
            billSell.setCode(topBillSell.getCode() + 1);
        }
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        billSell.setCustomer(offer.getCustomer());
        billSell.setDiscount(offer.getDiscount());
        billSell.setTransferFees(offer.getTransferFees());
        billSell.setNote(offer.getNote());
        billSell.setWrittenDate(new DateTime().toDate());
        billSell.setPerson(caller);
        billSell = billSellService.save(billSell);

        LOG.info("CONNECT OFFER PRODUCTS WITH BILL SELL");
        ListIterator<OfferProduct> offerProductListIterator = offer.getOfferProducts().listIterator();
        while (offerProductListIterator.hasNext()) {
            OfferProduct offerProduct = offerProductListIterator.next();
            BillSellProduct billSellProduct = new BillSellProduct();
            billSellProduct.setBillSell(billSell);
            billSellProduct.setProduct(offerProduct.getProduct());
            billSellProduct.setQuantity(offerProduct.getQuantity());
            billSellProduct.setUnitSellPrice(offerProduct.getUnitSellPrice());
            billSellProduct.setUnitVat(offerProduct.getUnitVat());
            billSellProduct.setDate(new DateTime().toDate());
            billSell.getBillSellProducts().add(billSellProductService.save(billSellProduct));
        }

        LOG.info("CHANGE OFFER STATE TO AGREED");
        offer.setCondition(OfferCondition.Agreed);
        offerService.save(offer);
        StringBuilder builder = new StringBuilder();
        builder.append("تمت الموافقة على العرض رقم ");
        builder.append(" ( " + offer.getCode() + " ) ");
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير البيع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.information)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        builder = new StringBuilder();
        builder.append("تحويل العرض رقم / ");
        builder.append(offer.getCode());
        builder.append("إلى فاتورة بيع رقم / ");
        builder.append(billSell.getCode());
        builder.append("، بمجموع أسعار = ");
        builder.append(billSell.getTotalPrice());
        builder.append("، وخصم بمقدار ");
        builder.append(billSell.getDiscount());
        builder.append("، وقيمة مضافة بمقدار ");
        builder.append(billSell.getTotalVat());
        builder.append("، وأصناف عدد " + billSell.getBillSellProducts().size() + " صنف");
        builder.append("، للعميل / ");
        builder.append(billSell.getCustomer().getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير البيع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSell);
    }

    @PostMapping(value = "createFromOrder", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_CREATE')")
    @Transactional
    public String createFromOrder(@RequestBody OrderSell orderSell) {
        orderSell = orderSellService.findOne(orderSell.getId());
        BillSell billSell = new BillSell();
        BillSell topBillSell = billSellService.findTopByOrderByCodeDesc();
        if (topBillSell == null) {
            billSell.setCode(Long.valueOf(1));
        }else{
            billSell.setCode(topBillSell.getCode() + 1);
        }
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        billSell.setCustomer(orderSell.getCustomer());
        billSell.setDiscount(orderSell.getDiscount());
        billSell.setTransferFees(orderSell.getTransferFees());
        billSell.setNote(orderSell.getRules());
        billSell.setWrittenDate(new DateTime().toDate());
        billSell.setPerson(caller);
        billSell = billSellService.save(billSell);

        LOG.info("CONNECT ORDER PRODUCTS WITH BILL SELL");
        ListIterator<OrderSellProduct> orderSellProductListIterator = orderSell.getOrderSellProducts().listIterator();
        while (orderSellProductListIterator.hasNext()) {
            OrderSellProduct orderSellProduct = orderSellProductListIterator.next();
            BillSellProduct billSellProduct = new BillSellProduct();
            billSellProduct.setBillSell(billSell);
            billSellProduct.setProduct(orderSellProduct.getProduct());
            billSellProduct.setQuantity(orderSellProduct.getQuantity());
            billSellProduct.setUnitSellPrice(orderSellProduct.getUnitSellPrice());
            billSellProduct.setUnitVat(orderSellProduct.getUnitVat());
            //TO-DO
            billSellProduct.setUnitSellPrice(orderSellProduct.getUnitSellPrice());
            billSellProduct.setDate(new DateTime().toDate());
            billSell.getBillSellProducts().add(billSellProductService.save(billSellProduct));
        }

        LOG.info("CHANGE ORDER STATE TO BILLED");
        orderSell.setCondition(OrderSellCondition.Agreed);
        orderSellService.save(orderSell);
        StringBuilder builder = new StringBuilder();
        builder.append("تمت الموافقة على أمر البيع رقم ");
        builder.append(" ( " + orderSell.getCode() + " ) ");
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير البيع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.information)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        builder = new StringBuilder();
        builder.append("تحويل أمر البيع رقم / ");
        builder.append(orderSell.getCode());
        builder.append("إلى فاتورة بيع رقم / ");
        builder.append(billSell.getCode());
        builder.append("، بمجموع أسعار = ");
        builder.append(billSell.getTotalPrice());
        builder.append("، وخصم بمقدار ");
        builder.append(billSell.getDiscount());
        builder.append("، وقيمة مضافة بمقدار ");
        builder.append(billSell.getTotalVat());
        builder.append("، وأصناف عدد " + billSell.getBillSellProducts().size() + " صنف");
        builder.append("، للعميل / ");
        builder.append(billSell.getCustomer().getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير البيع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSell);
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        BillSell billSell = billSellService.findOne(id);
        if (billSell != null) {
            LOG.info("DELETE ALL PRODUCTS RELATED WITH THIS BILL...");
            billSellProductService.delete(billSell.getBillSellProducts());
            LOG.info("DELETE THIS BILL...");
            billSellService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف الفاتورة رقم / ");
            builder.append(billSell.getCode());
            builder.append("، بقيمة : ");
            builder.append(billSell.getTotalPrice());
            builder.append("ريال سعودي، ");
            builder.append("، بتاريخ ");
            builder.append(DateConverter.getDateInFormat(billSell.getWrittenDate()));
            builder.append("للعميل / ");
            builder.append(billSell.getCustomer().getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على فواتير البيع")
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
                                       billSellService.findOne(id));
    }

    @GetMapping(value = "findByCustomer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByCustomer(@PathVariable(value = "customerId") Long customerId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETAILS),
                                       billSellService.findByCustomer(customerService.findOne(customerId)));
    }

    @GetMapping(value = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            //BillSell Filters
            @RequestParam(value = "codeFrom", required = false) final Integer codeFrom,
            @RequestParam(value = "codeTo", required = false) final Integer codeTo,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            //Customer Filters
            @RequestParam(value = "customerCodeFrom", required = false) final Integer customerCodeFrom,
            @RequestParam(value = "customerCodeTo", required = false) final Integer customerCodeTo,
            @RequestParam(value = "customerRegisterDateFrom", required = false) final Long customerRegisterDateFrom,
            @RequestParam(value = "customerRegisterDateTo", required = false) final Long customerRegisterDateTo,
            @RequestParam(value = "customerName", required = false) final String customerName,
            @RequestParam(value = "customerMobile", required = false) final String customerMobile,
            @RequestParam(value = "filterCompareType", required = false) final String filterCompareType,
            Pageable pageable) {
        return SquigglyUtils.stringify(
                Squiggly.init(
                        new ObjectMapper(),
                        "**,".concat("content[").concat(FILTER_TABLE).concat("]")),
                billSellSearch.filter(
                        codeFrom,
                        codeTo,
                        dateFrom,
                        dateTo,
                        customerCodeFrom,
                        customerCodeTo,
                        customerRegisterDateFrom,
                        customerRegisterDateTo,
                        customerName,
                        customerMobile,
                        filterCompareType,
                        pageable));
    }
}
