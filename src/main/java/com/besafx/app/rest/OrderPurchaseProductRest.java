package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.entity.OrderPurchaseProduct;
import com.besafx.app.service.OrderPurchaseProductService;
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
@RequestMapping(value = "/api/orderPurchaseProduct/")
public class OrderPurchaseProductRest {

    private final static Logger LOG = LoggerFactory.getLogger(OrderPurchaseProductRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "product[id,code,name]," +
            "orderPurchase[id,supplier[id,contact[id,shortName]]]";

    @Autowired
    private OrderPurchaseProductService orderPurchaseProductService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_PURCHASE_PRODUCT_CREATE')")
    @Transactional
    public String create(@RequestBody OrderPurchaseProduct orderPurchaseProduct) {
        orderPurchaseProduct.setDate(new DateTime().toDate());
        orderPurchaseProduct = orderPurchaseProductService.save(orderPurchaseProduct);

        StringBuilder builder = new StringBuilder();
        builder.append("اضافة السلعة / ");
        builder.append(orderPurchaseProduct.getProduct().getName());
        builder.append("، بكمية = ");
        builder.append(orderPurchaseProduct.getQuantity());
        builder.append("، سعر الشراء/الوحدة ");
        builder.append(orderPurchaseProduct.getUnitPurchasePrice());
        builder.append("، القيمة المضافة/الوحدة ");
        builder.append(orderPurchaseProduct.getUnitVat());
        builder.append("، لأمر الشراء رقم / ");
        builder.append(orderPurchaseProduct.getOrderPurchase().getCode());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على أومر الشراء")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderPurchaseProduct);
    }

    @PostMapping(value = "createBatch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_PURCHASE_PRODUCT_CREATE')")
    @Transactional
    public String createBatch(@RequestBody List<OrderPurchaseProduct> orderPurchaseProducts) {
        orderPurchaseProducts.stream().forEach(orderPurchaseProduct -> orderPurchaseProduct.setDate(new DateTime().toDate()));
        orderPurchaseProducts = Lists.newArrayList(orderPurchaseProductService.save(orderPurchaseProducts));

        StringBuilder builder = new StringBuilder();
        builder.append("اضافة السلع / ");
        builder.append(String.join("، ", orderPurchaseProducts.stream()
                                                             .map(orderPurchaseProduct -> orderPurchaseProduct.getProduct().getName())
                                                             .collect(Collectors.toList())));
        builder.append("، بكميات = ");
        builder.append(String.join("، ", orderPurchaseProducts.stream()
                                                             .map(orderPurchaseProduct -> orderPurchaseProduct.getQuantity().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، سعر الشراء/الوحدة ");
        builder.append(String.join("، ", orderPurchaseProducts.stream()
                                                             .map(orderPurchaseProduct -> orderPurchaseProduct.getUnitPurchasePrice().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، القيمة المضافة/الوحدة ");
        builder.append(String.join("، ", orderPurchaseProducts.stream()
                                                             .map(orderPurchaseProduct -> orderPurchaseProduct.getUnitVat().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، لأوامر الشراء رقم / ");
        builder.append(String.join("، ", orderPurchaseProducts.stream()
                                                             .map(orderPurchaseProduct -> orderPurchaseProduct.getOrderPurchase().getCode().toString())
                                                             .distinct()
                                                             .collect(Collectors.toList())));
        builder.append("، بالتوالي.");
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على أومر الشراء")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderPurchaseProducts);
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_PURCHASE_PRODUCT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        OrderPurchaseProduct orderPurchaseProduct = orderPurchaseProductService.findOne(id);
        if (orderPurchaseProduct != null) {
            orderPurchaseProductService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف السلعة / ");
            builder.append(orderPurchaseProduct.getProduct().getName());
            builder.append("، بكمية = ");
            builder.append(orderPurchaseProduct.getQuantity());
            builder.append("، سعر الشراء/الوحدة ");
            builder.append(orderPurchaseProduct.getUnitPurchasePrice());
            builder.append("، القيمة المضافة/الوحدة ");
            builder.append(orderPurchaseProduct.getUnitVat());
            builder.append("، من أمر الشراء رقم / ");
            builder.append(orderPurchaseProduct.getOrderPurchase().getCode());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على أومر الشراء")
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
                                       Lists.newArrayList(orderPurchaseProductService.findAll()));
    }

    @GetMapping(value = "findOne/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       orderPurchaseProductService.findOne(id));
    }

    @GetMapping(value = "findByOrderPurchase/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByOrderPurchase(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       orderPurchaseProductService.findByOrderPurchaseId(id));
    }

    @GetMapping(value = "findByProduct/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByProduct(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       orderPurchaseProductService.findByProductId(id));
    }
}
