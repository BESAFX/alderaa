package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.entity.BillSellProduct;
import com.besafx.app.service.BillSellProductService;
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
@RequestMapping(value = "/api/billSellProduct/")
public class BillSellProductRest {

    private final static Logger LOG = LoggerFactory.getLogger(BillSellProductRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "product[id,code,name]," +
            "billSell[id,code,customer[id,contact[id,shortName]]]";

    @Autowired
    private BillSellProductService billSellProductService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_PRODUCT_CREATE')")
    @Transactional
    public String create(@RequestBody BillSellProduct billSellProduct) {
        billSellProduct.setDate(new DateTime().toDate());
        billSellProduct = billSellProductService.save(billSellProduct);

        StringBuilder builder = new StringBuilder();
        builder.append("اضافة السلعة / ");
        builder.append(billSellProduct.getProduct().getName());
        builder.append("، بكمية = ");
        builder.append(billSellProduct.getQuantity());
        builder.append("، سعر البيع/الوحدة ");
        builder.append(billSellProduct.getUnitSellPrice());
        builder.append("، القيمة المضافة/الوحدة ");
        builder.append(billSellProduct.getUnitVat());
        builder.append("، لفاتورة البيع رقم / ");
        builder.append(billSellProduct.getBillSell().getCode());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير البيع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSellProduct);
    }

    @PostMapping(value = "createBatch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_PRODUCT_CREATE')")
    @Transactional
    public String createBatch(@RequestBody List<BillSellProduct> billSellProducts) {
        billSellProducts.stream().forEach(billSellProduct -> billSellProduct.setDate(new DateTime().toDate()));
        billSellProducts = Lists.newArrayList(billSellProductService.save(billSellProducts));

        StringBuilder builder = new StringBuilder();
        builder.append("اضافة السلع / ");
        builder.append(String.join("، ", billSellProducts.stream()
                                                             .map(billSellProduct -> billSellProduct.getProduct().getName())
                                                             .collect(Collectors.toList())));
        builder.append("، بكميات = ");
        builder.append(String.join("، ", billSellProducts.stream()
                                                             .map(billSellProduct -> billSellProduct.getQuantity().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، سعر البيع/الوحدة ");
        builder.append(String.join("، ", billSellProducts.stream()
                                                             .map(billSellProduct -> billSellProduct.getUnitSellPrice().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، القيمة المضافة/الوحدة ");
        builder.append(String.join("، ", billSellProducts.stream()
                                                             .map(billSellProduct -> billSellProduct.getUnitVat().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، لفواتير البيع رقم / ");
        builder.append(String.join("، ", billSellProducts.stream()
                                                             .map(billSellProduct -> billSellProduct.getBillSell().getCode().toString())
                                                             .distinct()
                                                             .collect(Collectors.toList())));
        builder.append("، بالتوالي.");
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على فواتير البيع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSellProducts);
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_PRODUCT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        BillSellProduct billSellProduct = billSellProductService.findOne(id);
        if (billSellProduct != null) {
            billSellProductService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف السلعة / ");
            builder.append(billSellProduct.getProduct().getName());
            builder.append("، بكمية = ");
            builder.append(billSellProduct.getQuantity());
            builder.append("، سعر البيع/الوحدة ");
            builder.append(billSellProduct.getUnitSellPrice());
            builder.append("، القيمة المضافة/الوحدة ");
            builder.append(billSellProduct.getUnitVat());
            builder.append("، لفاتورة البيع رقم / ");
            builder.append(billSellProduct.getBillSell().getCode());
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

    @GetMapping(value = "findAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       Lists.newArrayList(billSellProductService.findAll()));
    }

    @GetMapping(value = "findOne/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       billSellProductService.findOne(id));
    }

    @GetMapping(value = "findByBillSell/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByBillSell(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       billSellProductService.findByBillSellId(id));
    }

    @GetMapping(value = "findByProduct/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByProduct(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       billSellProductService.findByProductId(id));
    }
}
