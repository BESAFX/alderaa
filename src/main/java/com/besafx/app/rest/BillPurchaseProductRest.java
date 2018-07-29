package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.entity.BillPurchaseProduct;
import com.besafx.app.service.BillPurchaseProductService;
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
@RequestMapping(value = "/api/billPurchaseProduct/")
public class BillPurchaseProductRest {

    private final static Logger LOG = LoggerFactory.getLogger(BillPurchaseProductRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "product[id,code,name]," +
            "billPurchase[id,code,supplier[id,contact[id,shortName]]]";

    @Autowired
    private BillPurchaseProductService billPurchaseProductService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_PURCHASE_PRODUCT_CREATE')")
    @Transactional
    public String create(@RequestBody BillPurchaseProduct billPurchaseProduct) {
        billPurchaseProduct.setDate(new DateTime().toDate());
        billPurchaseProduct = billPurchaseProductService.save(billPurchaseProduct);

        StringBuilder builder = new StringBuilder();
        builder.append("اضافة السلعة / ");
        builder.append(billPurchaseProduct.getProduct().getName());
        builder.append("، بكمية = ");
        builder.append(billPurchaseProduct.getQuantity());
        builder.append("، سعر الشراء/الوحدة ");
        builder.append(billPurchaseProduct.getUnitPurchasePrice());
        builder.append("، سعر البيع/الوحدة ");
        builder.append(billPurchaseProduct.getUnitSellPrice());
        builder.append("، القيمة المضافة/الوحدة ");
        builder.append(billPurchaseProduct.getUnitVat());
        builder.append("، لفاتورة الشراء رقم / ");
        builder.append(billPurchaseProduct.getBillPurchase().getCode());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير الشراء")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billPurchaseProduct);
    }

    @PostMapping(value = "createBatch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_PURCHASE_PRODUCT_CREATE')")
    @Transactional
    public String createBatch(@RequestBody List<BillPurchaseProduct> billPurchaseProducts) {
        billPurchaseProducts.stream().forEach(billPurchaseProduct -> billPurchaseProduct.setDate(new DateTime().toDate()));
        billPurchaseProducts = Lists.newArrayList(billPurchaseProductService.save(billPurchaseProducts));

        StringBuilder builder = new StringBuilder();
        builder.append("اضافة السلع / ");
        builder.append(String.join("، ", billPurchaseProducts.stream()
                                                             .map(billPurchaseProduct -> billPurchaseProduct.getProduct().getName())
                                                             .collect(Collectors.toList())));
        builder.append("، بكميات = ");
        builder.append(String.join("، ", billPurchaseProducts.stream()
                                                             .map(billPurchaseProduct -> billPurchaseProduct.getQuantity().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، سعر الشراء/الوحدة ");
        builder.append(String.join("، ", billPurchaseProducts.stream()
                                                             .map(billPurchaseProduct -> billPurchaseProduct.getUnitPurchasePrice().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، سعر البيع/الوحدة ");
        builder.append(String.join("، ", billPurchaseProducts.stream()
                                                             .map(billPurchaseProduct -> billPurchaseProduct.getUnitSellPrice().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، القيمة المضافة/الوحدة ");
        builder.append(String.join("، ", billPurchaseProducts.stream()
                                                             .map(billPurchaseProduct -> billPurchaseProduct.getUnitVat().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، لفواتير الشراء رقم / ");
        builder.append(String.join("، ", billPurchaseProducts.stream()
                                                             .map(billPurchaseProduct -> billPurchaseProduct.getBillPurchase().getCode().toString())
                                                             .distinct()
                                                             .collect(Collectors.toList())));
        builder.append("، بالتوالي.");
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير الشراء")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billPurchaseProducts);
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_PURCHASE_PRODUCT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        BillPurchaseProduct billPurchaseProduct = billPurchaseProductService.findOne(id);
        if (billPurchaseProduct != null) {
            billPurchaseProductService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف السلعة / ");
            builder.append(billPurchaseProduct.getProduct().getName());
            builder.append("، بكمية = ");
            builder.append(billPurchaseProduct.getQuantity());
            builder.append("، سعر الشراء/الوحدة ");
            builder.append(billPurchaseProduct.getUnitPurchasePrice());
            builder.append("، سعر البيع/الوحدة ");
            builder.append(billPurchaseProduct.getUnitSellPrice());
            builder.append("، القيمة المضافة/الوحدة ");
            builder.append(billPurchaseProduct.getUnitVat());
            builder.append("، لفاتورة الشراء رقم / ");
            builder.append(billPurchaseProduct.getBillPurchase().getCode());
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

    @GetMapping(value = "findAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       Lists.newArrayList(billPurchaseProductService.findAll()));
    }

    @GetMapping(value = "findOne/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       billPurchaseProductService.findOne(id));
    }

    @GetMapping(value = "findByBillPurchase/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByBillPurchase(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       billPurchaseProductService.findByBillPurchaseId(id));
    }

    @GetMapping(value = "findByProduct/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByProduct(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       billPurchaseProductService.findByProductId(id));
    }
}
