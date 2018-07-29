package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Customer;
import com.besafx.app.entity.OrderSell;
import com.besafx.app.entity.OrderSellProduct;
import com.besafx.app.entity.Person;
import com.besafx.app.search.OrderSellSearch;
import com.besafx.app.service.CustomerService;
import com.besafx.app.service.OrderSellProductService;
import com.besafx.app.service.OrderSellService;
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
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ListIterator;

@RestController
@RequestMapping(value = "/api/orderSell/")
public class OrderSellRest {

    private final static Logger LOG = LoggerFactory.getLogger(OrderSellRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "customer[id,contact[id,mobile,shortName]]," +
            "-orderSellProducts," +
            "-orderSellAttaches," +
            "person[id,contact[id,shortName]]";

    private final String FILTER_DETAILS = "" +
            "**," +
            "customer[id,contact[id,mobile,shortName]]," +
            "orderSellProducts[**,-orderSell,product[id,name]]," +
            "orderSellAttaches[**,-orderSell,attach[**,person[id,contact[shortName]]]]," +
            "person[id,contact[id,shortName]]";

    @Autowired
    private OrderSellService orderSellService;

    @Autowired
    private OrderSellProductService orderSellProductService;

    @Autowired
    private OrderSellSearch orderSellSearch;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_SELL_CREATE')")
    @Transactional
    public String create(@RequestBody OrderSell orderSell) {
        OrderSell topOrderSell = orderSellService.findTopByOrderByCodeDesc();
        if (topOrderSell == null) {
            orderSell.setCode(Long.valueOf(1));
        } else {
            orderSell.setCode(topOrderSell.getCode() + 1);
        }

        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        orderSell.setWrittenDate(new DateTime().toDate());
        orderSell.setPerson(caller);
        orderSell = orderSellService.save(orderSell);

        LOG.info("CONNECT PRODUCTS WITH ORDER");
        ListIterator<OrderSellProduct> orderSellProductListIterator = orderSell.getOrderSellProducts().listIterator();
        while (orderSellProductListIterator.hasNext()) {
            OrderSellProduct orderSellProduct = orderSellProductListIterator.next();
            orderSellProduct.setDate(new DateTime().toDate());
            orderSellProduct.setOrderSell(orderSell);
            orderSellProductListIterator.set(orderSellProductService.save(orderSellProduct));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("انشاء أمر بيع رقم / ");
        builder.append(orderSell.getCode());
        builder.append("، بمجموع أسعار = ");
        builder.append(orderSell.getTotalPrice());
        builder.append("، وخصم بمقدار ");
        builder.append(orderSell.getDiscount());
        builder.append("، وقيمة مضافة بمقدار ");
        builder.append(orderSell.getTotalVat());
        builder.append("، وأصناف عدد " + orderSell.getOrderSellProducts().size() + " صنف");
        builder.append("، للعميل / ");
        builder.append(orderSell.getCustomer().getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على أوامر البيع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderSell);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_SELL_UPDATE')")
    @Transactional
    public String update(@RequestBody OrderSell orderSell) {
        if (orderSellService.findByCodeAndIdIsNot(orderSell.getCode(), orderSell.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        OrderSell object = orderSellService.findOne(orderSell.getId());
        if (object != null) {
            orderSell = orderSellService.save(orderSell);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("تعديل بيانات أمر البيع رقم ");
            builder.append("( " + orderSell.getCode() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على أوامر البيع")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.warning)
                                                  .icon("edit")
                                                  .build());
            entityHistoryListener.perform(builder.toString());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderSell);
        } else {
            return null;
        }
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_SELL_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        OrderSell orderSell = orderSellService.findOne(id);
        if (orderSell != null) {
            LOG.info("DELETE ALL PRODUCTS RELATED WITH THIS ORDER_SELL...");
            orderSellProductService.delete(orderSell.getOrderSellProducts());
            LOG.info("DELETE THIS ORDER_SELL...");
            orderSellService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف أمر بيع رقم / ");
            builder.append(orderSell.getCode());
            builder.append("، بقيمة : ");
            builder.append(orderSell.getTotalPrice());
            builder.append("ريال سعودي، ");
            builder.append("، بتاريخ ");
            builder.append(DateConverter.getDateInFormat(orderSell.getWrittenDate()));
            builder.append("للعميل / ");
            builder.append(orderSell.getCustomer().getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على أوامر البيع")
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
                                       orderSellService.findOne(id));
    }

    @GetMapping(value = "findByCustomer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByCustomer(@PathVariable(value = "customerId") Long customerId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETAILS),
                                       orderSellService.findByCustomer(customerService.findOne(customerId)));
    }

    @GetMapping(value = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            //OrderSell Filters
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
                orderSellSearch.filter(
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
