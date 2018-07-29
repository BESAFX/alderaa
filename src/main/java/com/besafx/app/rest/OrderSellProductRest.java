package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.entity.OrderSellProduct;
import com.besafx.app.service.OrderSellProductService;
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
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/orderSellProduct/")
public class OrderSellProductRest {

    private final static Logger LOG = LoggerFactory.getLogger(OrderSellProductRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "product[id,code,name]," +
            "orderSell[id,customer[id,contact[id,shortName]]]";

    @Autowired
    private OrderSellProductService orderSellProductService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_SELL_PRODUCT_CREATE')")
    @Transactional
    public String create(@RequestBody OrderSellProduct orderSellProduct) {
        orderSellProduct.setDate(new DateTime().toDate());
        orderSellProduct = orderSellProductService.save(orderSellProduct);

        StringBuilder builder = new StringBuilder();
        builder.append("اضافة السلعة / ");
        builder.append(orderSellProduct.getProduct().getName());
        builder.append("، بكمية = ");
        builder.append(orderSellProduct.getQuantity());
        builder.append("، سعر البيع/الوحدة ");
        builder.append(orderSellProduct.getUnitSellPrice());
        builder.append("، القيمة المضافة/الوحدة ");
        builder.append(orderSellProduct.getUnitVat());
        builder.append("، لأمر البيع رقم / ");
        builder.append(orderSellProduct.getOrderSell().getCode());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على أومر البيع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderSellProduct);
    }

    @PostMapping(value = "createBatch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_SELL_PRODUCT_CREATE')")
    @Transactional
    public String createBatch(@RequestBody List<OrderSellProduct> orderSellProducts) {
        orderSellProducts.stream().forEach(orderSellProduct -> orderSellProduct.setDate(new DateTime().toDate()));
        orderSellProducts = Lists.newArrayList(orderSellProductService.save(orderSellProducts));

        StringBuilder builder = new StringBuilder();
        builder.append("اضافة السلع / ");
        builder.append(String.join("، ", orderSellProducts.stream()
                                                             .map(orderSellProduct -> orderSellProduct.getProduct().getName())
                                                             .collect(Collectors.toList())));
        builder.append("، بكميات = ");
        builder.append(String.join("، ", orderSellProducts.stream()
                                                             .map(orderSellProduct -> orderSellProduct.getQuantity().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، سعر البيع/الوحدة ");
        builder.append(String.join("، ", orderSellProducts.stream()
                                                             .map(orderSellProduct -> orderSellProduct.getUnitSellPrice().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، القيمة المضافة/الوحدة ");
        builder.append(String.join("، ", orderSellProducts.stream()
                                                             .map(orderSellProduct -> orderSellProduct.getUnitVat().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، لأوامر البيع رقم / ");
        builder.append(String.join("، ", orderSellProducts.stream()
                                                             .map(orderSellProduct -> orderSellProduct.getOrderSell().getCode().toString())
                                                             .distinct()
                                                             .collect(Collectors.toList())));
        builder.append("، بالتوالي.");
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على أومر البيع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderSellProducts);
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_SELL_PRODUCT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        OrderSellProduct orderSellProduct = orderSellProductService.findOne(id);
        if (orderSellProduct != null) {
            orderSellProductService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف السلعة / ");
            builder.append(orderSellProduct.getProduct().getName());
            builder.append("، بكمية = ");
            builder.append(orderSellProduct.getQuantity());
            builder.append("، سعر البيع/الوحدة ");
            builder.append(orderSellProduct.getUnitSellPrice());
            builder.append("، القيمة المضافة/الوحدة ");
            builder.append(orderSellProduct.getUnitVat());
            builder.append("، من أمر البيع رقم / ");
            builder.append(orderSellProduct.getOrderSell().getCode());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على أومر البيع")
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
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       Lists.newArrayList(orderSellProductService.findAll()));
    }

    @GetMapping(value = "findOne/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       orderSellProductService.findOne(id));
    }

    @GetMapping(value = "findByOrderSell/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByOrderSell(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       orderSellProductService.findByOrderSellId(id));
    }

    @GetMapping(value = "findByProduct/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByProduct(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       orderSellProductService.findByProductId(id));
    }
}
