package com.besafx.app.rest;

import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Product;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Product;
import com.besafx.app.search.ProductSearch;
import com.besafx.app.service.ProductService;
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
import java.util.List;
import java.util.ListIterator;

@RestController
@RequestMapping(value = "/api/product/")
public class ProductRest {

    private final static Logger LOG = LoggerFactory.getLogger(ProductRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "person[id,contact[id,shortName]]," +
            "parent[id,code,name]," +
            "childs[id,name]," +
            "-billPurchaseProducts," +
            "-billSellProducts";

    private final String FILTER_PARENT = "" +
            "id," +
            "code," +
            "name," +
            "childs[id]";

    private final String FILTER_CHILD = "" +
            "id," +
            "code," +
            "name," +
            "parent[id,name]";

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSearch productSearch;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PRODUCT_CREATE')")
    @Transactional
    public String create(@RequestBody Product product) {
        Product topProduct = productService.findTopByOrderByCodeDesc();
        if (topProduct == null) {
            product.setCode(1);
        } else {
            product.setCode(topProduct.getCode() + 1);
        }
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        product.setEnabled(true);
        product.setRegisterDate(new DateTime().toDate());
        product.setPerson(caller);
        product = productService.save(product);

        StringBuilder builder = new StringBuilder();
        builder.append("إنشاء التصنيف رقم ");
        builder.append("( " + product.getCode() + " )");
        builder.append("، بواسطة ");
        builder.append(caller.getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على السلع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());

        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), product);
    }

    @PostMapping(value = "createBatch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PRODUCT_CREATE')")
    @Transactional
    public String createBatch(@RequestBody List<Product> products) {
        Product topProduct = productService.findTopByOrderByCodeDesc();
        Integer lastCode;
        if (topProduct == null) {
            lastCode = 1;
        } else {
            lastCode = topProduct.getCode() + 1;
        }
        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        ListIterator<Product> productListIterator = products.listIterator();
        while (productListIterator.hasNext()) {
            Product product = productListIterator.next();
            product.setCode(lastCode++);
            product.setEnabled(true);
            product.setPerson(caller);
            LOG.info("CHECK PARENT IS EXIST");
            if(product.getParent() != null){
                Product parent = productService.findByName(product.getParent().getName());
                if(parent == null){
                    LOG.info("SAVE PARENT");
                    Product newParent = product.getParent();
                    newParent.setCode(lastCode++);
                    newParent.setParent(null);
                    newParent.setRegisterDate(product.getRegisterDate());
                    newParent.setEnabled(true);
                    newParent.setPerson(caller);
                    product.setParent(productService.save(newParent));
                }else{
                    product.setParent(parent);
                }
            }else{
                productListIterator.remove();
            }
        }

        LOG.info("SAVE PRODUCTS");
        productService.save(products);

        StringBuilder builder = new StringBuilder();
        builder.append("إنشاء مجموعة من الأصناف عدد ");
        builder.append("( " + products.size() + " )");
        builder.append("، بواسطة ");
        builder.append(caller.getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على السلع")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());

        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), products);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PRODUCT_UPDATE')")
    @Transactional
    public String update(@RequestBody Product product) {
        if (productService.findByCodeAndIdIsNot(product.getCode(), product.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Product object = productService.findOne(product.getId());
        if (object != null) {
            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            product.setEnabled(true);
            product.setRegisterDate(new DateTime().toDate());
            product.setPerson(caller);
            product = productService.save(product);

            StringBuilder builder = new StringBuilder();
            builder.append("تعديل بيانات التصنيف رقم ");
            builder.append("( " + product.getCode() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على السلع")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.warning)
                                                  .icon("edit")
                                                  .build());

            entityHistoryListener.perform(builder.toString());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), product);
        } else {
            return null;
        }
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PRODUCT_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        Product product = productService.findOne(id);
        if (product != null) {
            productService.delete(id);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("حذف التصنيف رقم ");
            builder.append("( " + product.getCode() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على السلع")
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
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                                       productService.findOne(id));
    }

    @GetMapping(value = "findParents", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findParents() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_PARENT),
                                       productService.findByParentIsNull());
    }

    @GetMapping(value = "findChilds/{parentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findChilds(@PathVariable(value = "parentId") Long parentId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_CHILD),
                                       productService.findByParentId(parentId));
    }

    @GetMapping(value = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            //Product Filters
            @RequestParam(value = "codeFrom", required = false) final Integer codeFrom,
            @RequestParam(value = "codeTo", required = false) final Integer codeTo,
            @RequestParam(value = "registerDateFrom", required = false) final Long registerDateFrom,
            @RequestParam(value = "registerDateTo", required = false) final Long registerDateTo,
            @RequestParam(value = "name", required = false) final String name,
            @RequestParam(value = "parentId", required = false) final Long parentId,
            Pageable pageable) {
        return SquigglyUtils.stringify(
                Squiggly.init(
                        new ObjectMapper(),
                        "**,".concat("content[").concat(FILTER_TABLE).concat("]")),
                productSearch.filter(
                        codeFrom,
                        codeTo,
                        registerDateFrom,
                        registerDateTo,
                        name,
                        parentId,
                        pageable));
    }
}
