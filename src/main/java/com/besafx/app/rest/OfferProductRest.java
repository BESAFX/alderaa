package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.entity.OfferProduct;
import com.besafx.app.service.OfferProductService;
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
@RequestMapping(value = "/api/offerProduct/")
public class OfferProductRest {

    private final static Logger LOG = LoggerFactory.getLogger(OfferProductRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "product[id,code,name]," +
            "offer[id,supplier[id,contact[id,shortName]]]";

    @Autowired
    private OfferProductService offerProductService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OFFER_PRODUCT_CREATE')")
    @Transactional
    public String create(@RequestBody OfferProduct offerProduct) {
        offerProduct.setDate(new DateTime().toDate());
        offerProduct = offerProductService.save(offerProduct);

        StringBuilder builder = new StringBuilder();
        builder.append("اضافة السلعة / ");
        builder.append(offerProduct.getProduct().getName());
        builder.append("، بكمية = ");
        builder.append(offerProduct.getQuantity());
        builder.append("، سعر البيع/الوحدة ");
        builder.append(offerProduct.getUnitSellPrice());
        builder.append("، القيمة المضافة/الوحدة ");
        builder.append(offerProduct.getUnitVat());
        builder.append("، للعرض رقم / ");
        builder.append(offerProduct.getOffer().getCode());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على العروض")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), offerProduct);
    }

    @PostMapping(value = "createBatch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OFFER_PRODUCT_CREATE')")
    @Transactional
    public String createBatch(@RequestBody List<OfferProduct> offerProducts) {
        offerProducts.stream().forEach(offerProduct -> offerProduct.setDate(new DateTime().toDate()));
        offerProducts = Lists.newArrayList(offerProductService.save(offerProducts));

        StringBuilder builder = new StringBuilder();
        builder.append("اضافة السلع / ");
        builder.append(String.join("، ", offerProducts.stream()
                                                             .map(offerProduct -> offerProduct.getProduct().getName())
                                                             .collect(Collectors.toList())));
        builder.append("، بكميات = ");
        builder.append(String.join("، ", offerProducts.stream()
                                                             .map(offerProduct -> offerProduct.getQuantity().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، سعر البيع/الوحدة ");
        builder.append(String.join("، ", offerProducts.stream()
                                                             .map(offerProduct -> offerProduct.getUnitSellPrice().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، القيمة المضافة/الوحدة ");
        builder.append(String.join("، ", offerProducts.stream()
                                                             .map(offerProduct -> offerProduct.getUnitVat().toString())
                                                             .collect(Collectors.toList())));
        builder.append("، للعرض رقم / ");
        builder.append(String.join("، ", offerProducts.stream()
                                                             .map(offerProduct -> offerProduct.getOffer().getCode().toString())
                                                             .distinct()
                                                             .collect(Collectors.toList())));
        builder.append("، بالتوالي.");
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على العروض")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), offerProducts);
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OFFER_PRODUCT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        OfferProduct offerProduct = offerProductService.findOne(id);
        if (offerProduct != null) {
            offerProductService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف السلعة / ");
            builder.append(offerProduct.getProduct().getName());
            builder.append("، بكمية = ");
            builder.append(offerProduct.getQuantity());
            builder.append("، سعر البيع/الوحدة ");
            builder.append(offerProduct.getUnitSellPrice());
            builder.append("، القيمة المضافة/الوحدة ");
            builder.append(offerProduct.getUnitVat());
            builder.append("، للعرض رقم / ");
            builder.append(offerProduct.getOffer().getCode());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على العروض")
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
                                       Lists.newArrayList(offerProductService.findAll()));
    }

    @GetMapping(value = "findOne/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       offerProductService.findOne(id));
    }

    @GetMapping(value = "findByOffer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByOffer(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       offerProductService.findByOfferId(id));
    }

    @GetMapping(value = "findByProduct/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByProduct(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       offerProductService.findByProductId(id));
    }
}
