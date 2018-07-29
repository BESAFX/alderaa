package com.besafx.app.rest;

import com.besafx.app.async.AsyncOrderPurchaseGenerator;
import com.besafx.app.async.AsyncOrderPurchaseGenerator;
import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.component.QuickEmail;
import com.besafx.app.component.ReportExporter;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.*;
import com.besafx.app.entity.enums.OrderPurchaseCondition;
import com.besafx.app.enums.ExportType;
import com.besafx.app.init.Initializer;
import com.besafx.app.search.OrderPurchaseSearch;
import com.besafx.app.service.OrderPurchaseVerificationTokenService;
import com.besafx.app.service.SupplierService;
import com.besafx.app.service.OrderPurchaseProductService;
import com.besafx.app.service.OrderPurchaseService;
import com.besafx.app.util.CompanyOptions;
import com.besafx.app.util.DateConverter;
import com.besafx.app.util.JSONConverter;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/orderPurchase/")
public class OrderPurchaseRest {

    private final static Logger LOG = LoggerFactory.getLogger(OrderPurchaseRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "supplier[id,contact[id,mobile,shortName]]," +
            "-orderPurchaseProducts," +
            "-orderPurchaseAttaches," +
            "person[id,contact[id,shortName]]";

    private final String FILTER_DETAILS = "" +
            "**," +
            "supplier[id,contact[id,mobile,shortName]]," +
            "orderPurchaseProducts[**,-orderPurchase,product[id,name]]," +
            "orderPurchaseAttaches[**,-orderPurchase,attach[**,person[id,contact[shortName]]]]," +
            "person[id,contact[id,shortName]]";

    @Autowired
    private OrderPurchaseService orderPurchaseService;

    @Autowired
    private OrderPurchaseProductService orderPurchaseProductService;

    @Autowired
    private OrderPurchaseSearch orderPurchaseSearch;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @Autowired
    private QuickEmail quickEmail;

    @Autowired
    private ReportExporter reportExporter;

    @Autowired
    private OrderPurchaseVerificationTokenService orderPurchaseVerificationTokenService;

    @Autowired
    private AsyncOrderPurchaseGenerator asyncOrderPurchaseGenerator;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_PURCHASE_CREATE')")
    @Transactional
    public String create(@RequestBody OrderPurchase orderPurchase) {
        OrderPurchase topOrderPurchase = orderPurchaseService.findTopByOrderByCodeDesc();
        if (topOrderPurchase == null) {
            orderPurchase.setCode(Long.valueOf(1));
        } else {
            orderPurchase.setCode(topOrderPurchase.getCode() + 1);
        }

        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        orderPurchase.setWrittenDate(new DateTime().toDate());
        orderPurchase.setPerson(caller);
        orderPurchase = orderPurchaseService.save(orderPurchase);

        LOG.info("CONNECT PRODUCTS WITH ORDER");
        ListIterator<OrderPurchaseProduct> orderPurchaseProductListIterator = orderPurchase.getOrderPurchaseProducts().listIterator();
        while (orderPurchaseProductListIterator.hasNext()) {
            OrderPurchaseProduct orderPurchaseProduct = orderPurchaseProductListIterator.next();
            orderPurchaseProduct.setDate(new DateTime().toDate());
            orderPurchaseProduct.setOrderPurchase(orderPurchase);
            orderPurchaseProductListIterator.set(orderPurchaseProductService.save(orderPurchaseProduct));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("انشاء أمر شراء رقم / ");
        builder.append(orderPurchase.getCode());
        builder.append("، بمجموع أسعار = ");
        builder.append(orderPurchase.getTotalPrice());
        builder.append("، وخصم بمقدار ");
        builder.append(orderPurchase.getDiscount());
        builder.append("، وقيمة مضافة بمقدار ");
        builder.append(orderPurchase.getTotalVat());
        builder.append("، وأصناف عدد " + orderPurchase.getOrderPurchaseProducts().size() + " صنف");
        builder.append("، للمورد / ");
        builder.append(orderPurchase.getSupplier().getContact().getShortName());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على أوامر الشراء")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderPurchase);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_PURCHASE_UPDATE')")
    @Transactional
    public String update(@RequestBody OrderPurchase orderPurchase) {
        if (orderPurchaseService.findByCodeAndIdIsNot(orderPurchase.getCode(), orderPurchase.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        OrderPurchase object = orderPurchaseService.findOne(orderPurchase.getId());
        if (object != null) {
            orderPurchase = orderPurchaseService.save(orderPurchase);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("تعديل بيانات أمر الشراء رقم ");
            builder.append("( " + orderPurchase.getCode() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على أوامر الشراء")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.warning)
                                                  .icon("edit")
                                                  .build());
            entityHistoryListener.perform(builder.toString());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderPurchase);
        } else {
            return null;
        }
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_PURCHASE_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        OrderPurchase orderPurchase = orderPurchaseService.findOne(id);
        if (orderPurchase != null) {
            LOG.info("DELETE ALL PRODUCTS RELATED WITH THIS ORDER_PURCHASE...");
            orderPurchaseProductService.delete(orderPurchase.getOrderPurchaseProducts());
            LOG.info("DELETE THIS ORDER_PURCHASE...");
            orderPurchaseService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف أمر شراء رقم / ");
            builder.append(orderPurchase.getCode());
            builder.append("، بقيمة : ");
            builder.append(orderPurchase.getTotalPrice());
            builder.append("ريال سعودي، ");
            builder.append("، بتاريخ ");
            builder.append(DateConverter.getDateInFormat(orderPurchase.getWrittenDate()));
            builder.append("للمورد / ");
            builder.append(orderPurchase.getSupplier().getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على أوامر الشراء")
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
                                       orderPurchaseService.findOne(id));
    }

    @GetMapping(value = "findBySupplier/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findBySupplier(@PathVariable(value = "supplierId") Long supplierId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETAILS),
                                       orderPurchaseService.findBySupplier(supplierService.findOne(supplierId)));
    }

    @GetMapping(value = "send/{orderPurchaseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void send(@PathVariable(value = "orderPurchaseId") Long orderPurchaseId, HttpServletRequest request) {
        OrderPurchase orderPurchase = orderPurchaseService.findOne(orderPurchaseId);
        if (orderPurchase != null) {
            LOG.info("CREATING ORDER PURCHASE TOKEN...");
            String token = UUID.randomUUID().toString();
            OrderPurchaseVerificationToken orderPurchaseVerificationToken = new OrderPurchaseVerificationToken();
            orderPurchaseVerificationToken.setOrderPurchase(orderPurchase);
            orderPurchaseVerificationToken.setToken(token);
            orderPurchaseVerificationTokenService.save(orderPurchaseVerificationToken);

            CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
            LOG.info("SENDING TOKEN TO CUSTOMER...");
            String subject = "أمر شراء رقم " + " ( " + orderPurchase.getCode() + " ) ";
            List<String> emails = Lists.newArrayList(orderPurchase.getSupplier().getEmail());
            String title = options.getReportTitle();
            String subTitle = options.getReportSubTitle();
            String body = "نرجو من حضراتكم الموافقة على أمر الشراء المرفق وإعلامنا بالنتيجة ولكم جزيل الشكر والتقدير";
            String baseUrl = String.format("%s://%s:%d/", request.getScheme(), request.getServerName(), request.getServerPort());
            String buttonLink = baseUrl + "api/orderPurchase/view?orderPurchaseId=" + orderPurchaseId + "&token=" + token;
            String buttonText = "عرض أمر الشراء";
            quickEmail.send(subject, emails, title, subTitle, body, buttonLink, buttonText);
            LOG.info(buttonLink);

            LOG.info("UPDATE ORDER PURCHASE STATE TO SENT");
            orderPurchase.setCondition(OrderPurchaseCondition.Sent);
            orderPurchaseService.save(orderPurchase);
        } else {
            throw new CustomException("عفواً، لا يوجد هذا الأمر بقاعدة البيانات");
        }
    }

    @GetMapping(value = "/view", produces = "application/pdf")
    @ResponseBody
    public void view(@RequestParam(value = "orderPurchaseId") Long orderPurchaseId,
                     @RequestParam(value = "token") String token,
                     HttpServletResponse response) throws Exception {
        OrderPurchase orderPurchase = orderPurchaseService.findOne(orderPurchaseId);
        if(orderPurchase != null) {
            CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
            LOG.info("SKIP VERIFICATION IF TOKEN LENGTH IS ZERO");
            if(options.getTokenLengthInHours() == 0){
                reportExporter.export("أمر شراء", ExportType.PDF, response, asyncOrderPurchaseGenerator.generate(orderPurchaseId).get());
            }else{
                LOG.info("CHECK IF TOKEN IS NOT EXPIRED");
                OrderPurchaseVerificationToken orderPurchaseVerificationToken = orderPurchaseVerificationTokenService.findByOrderPurchaseAndToken(orderPurchase, token);
                if(orderPurchaseVerificationToken != null){
                    DateTime now = new DateTime();
                    if(new DateTime(orderPurchaseVerificationToken.getModifiedDate())
                            .plusHours(options.getTokenLengthInHours())
                            .isAfter(now)){
                        LOG.info("TOKEN STILL ACCEPTED, SHOW IT TO VISITOR");
                        reportExporter.export("أمر شراء", ExportType.PDF, response, asyncOrderPurchaseGenerator.generate(orderPurchaseId).get());
                        orderPurchase.setCondition(OrderPurchaseCondition.Opened);
                        orderPurchaseService.save(orderPurchase);
                    }else{
                        LOG.info("TOKEN NOT ACCEPTED ANYMORE-1");
                    }
                }else{
                    LOG.info("TOKEN NOT ACCEPTED ANYMORE-2");
                }
            }
        }else{
            LOG.info("ORDER PURCHASE NOT EXIST");
        }
    }

    @GetMapping(value = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            //OrderPurchase Filters
            @RequestParam(value = "codeFrom", required = false) final Integer codeFrom,
            @RequestParam(value = "codeTo", required = false) final Integer codeTo,
            @RequestParam(value = "writtenDateFrom", required = false) final Long writtenDateFrom,
            @RequestParam(value = "writtenDateTo", required = false) final Long writtenDateTo,
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
                orderPurchaseSearch.filter(
                        codeFrom,
                        codeTo,
                        writtenDateFrom,
                        writtenDateTo,
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
