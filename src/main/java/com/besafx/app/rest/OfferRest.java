package com.besafx.app.rest;

import com.besafx.app.async.AsyncOfferGenerator;
import com.besafx.app.auditing.EntityHistoryListener;
import com.besafx.app.auditing.PersonAwareUserDetails;
import com.besafx.app.component.QuickEmail;
import com.besafx.app.component.ReportExporter;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.*;
import com.besafx.app.entity.enums.OfferCondition;
import com.besafx.app.enums.ExportType;
import com.besafx.app.init.Initializer;
import com.besafx.app.search.OfferSearch;
import com.besafx.app.service.CustomerService;
import com.besafx.app.service.OfferProductService;
import com.besafx.app.service.OfferService;
import com.besafx.app.service.OfferVerificationTokenService;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
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
@RequestMapping(value = "/api/offer/")
public class OfferRest {

    private final static Logger LOG = LoggerFactory.getLogger(OfferRest.class);

    private final String FILTER_TABLE = "" +
            "**," +
            "customer[id,contact[id,mobile,shortName]]," +
            "-offerProducts," +
            "-offerAttaches," +
            "person[id,contact[id,shortName]]";

    private final String FILTER_DETAILS = "" +
            "**," +
            "customer[id,contact[id,mobile,shortName]]," +
            "offerProducts[**,-offer,product[id,name]]," +
            "offerAttaches[**,-offer,attach[**,person[id,contact[shortName]]]]," +
            "person[id,contact[id,shortName]]";

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferProductService offerProductService;

    @Autowired
    private OfferSearch offerSearch;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EntityHistoryListener entityHistoryListener;

    @Autowired
    private QuickEmail quickEmail;

    @Autowired
    private ReportExporter reportExporter;

    @Autowired
    private OfferVerificationTokenService offerVerificationTokenService;

    @Autowired
    private AsyncOfferGenerator asyncOfferGenerator;

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OFFER_CREATE')")
    @Transactional
    public String create(@RequestBody Offer offer) {
        Offer topOffer = offerService.findTopByOrderByCodeDesc();
        if (topOffer == null) {
            offer.setCode(Long.valueOf(1));
        } else {
            offer.setCode(topOffer.getCode() + 1);
        }

        Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
        offer.setWrittenDate(new DateTime().toDate());
        offer.setPerson(caller);
        offer = offerService.save(offer);

        LOG.info("ربط الأصناف المطلوبة مع العرض");
        ListIterator<OfferProduct> offerProductListIterator = offer.getOfferProducts().listIterator();
        while (offerProductListIterator.hasNext()) {
            OfferProduct offerProduct = offerProductListIterator.next();
            offerProduct.setDate(new DateTime().toDate());
            offerProduct.setOffer(offer);
            offerProductListIterator.set(offerProductService.save(offerProduct));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("انشاء عرض رقم / ");
        builder.append(offer.getCode());
        builder.append("، بمجموع أسعار = ");
        builder.append(offer.getTotalPrice());
        builder.append("، وخصم بمقدار ");
        builder.append(offer.getDiscount());
        builder.append("، وقيمة مضافة بمقدار ");
        builder.append(offer.getTotalVat());
        builder.append("، وأصناف عدد " + offer.getOfferProducts().size() + " صنف");
        builder.append("، للعميل / ");
        builder.append(offer.getCustomer().getContact().getShortName());
        builder.append("، ");
        builder.append(offer.getNote());
        notificationService.notifyAll(Notification
                                              .builder()
                                              .title("العمليات على العروض")
                                              .message(builder.toString())
                                              .type(NotificationDegree.success)
                                              .icon("add")
                                              .build());
        entityHistoryListener.perform(builder.toString());

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), offer);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OFFER_UPDATE')")
    @Transactional
    public String update(@RequestBody Offer offer) {
        if (offerService.findByCodeAndIdIsNot(offer.getCode(), offer.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Offer object = offerService.findOne(offer.getId());
        if (object != null) {
            offer = offerService.save(offer);

            Person caller = ((PersonAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();
            StringBuilder builder = new StringBuilder();
            builder.append("تعديل بيانات العرض رقم ");
            builder.append("( " + offer.getCode() + " )");
            builder.append("، بواسطة ");
            builder.append(caller.getContact().getShortName());
            notificationService.notifyAll(Notification
                                                  .builder()
                                                  .title("العمليات على العروض")
                                                  .message(builder.toString())
                                                  .type(NotificationDegree.warning)
                                                  .icon("edit")
                                                  .build());
            entityHistoryListener.perform(builder.toString());

            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), offer);
        } else {
            return null;
        }
    }

    @DeleteMapping(value = "delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OFFER_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        Offer offer = offerService.findOne(id);
        if (offer != null) {
            LOG.info("DELETE ALL PRODUCTS RELATED WITH THIS OFFER...");
            offerProductService.delete(offer.getOfferProducts());
            LOG.info("DELETE THIS OFFER...");
            offerService.delete(id);

            StringBuilder builder = new StringBuilder();
            builder.append("حذف العرض رقم / ");
            builder.append(offer.getCode());
            builder.append("، بقيمة : ");
            builder.append(offer.getTotalPrice());
            builder.append("ريال سعودي، ");
            builder.append("، بتاريخ ");
            builder.append(DateConverter.getDateInFormat(offer.getWrittenDate()));
            builder.append("للعميل / ");
            builder.append(offer.getCustomer().getContact().getShortName());
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

    @GetMapping(value = "findOne/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETAILS),
                                       offerService.findOne(id));
    }

    @GetMapping(value = "findByCustomer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByCustomer(@PathVariable(value = "customerId") Long customerId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETAILS),
                                       offerService.findByCustomer(customerService.findOne(customerId)));
    }

    @GetMapping(value = "findDaily", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findDaily() {
        DateTime startTime = new DateTime().withTimeAtStartOfDay();
        DateTime endTime = new DateTime().plusDays(1).withTimeAtStartOfDay();
        LOG.info("GETTING OFFERS WITHIN THIS DAY...");
        LOG.info("Start TIME: " + startTime.toString());
        LOG.info("End TIME  : " + endTime.toString());
        Specifications specifications = Specifications
                .where((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("writtenDate"), startTime.toDate()))
                .and((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("writtenDate"), endTime.toDate()));
        Sort sort = new Sort(Sort.Direction.DESC, "writtenDate");
        List<History> histories = offerService.findAll(specifications, sort);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), histories);
    }

    @GetMapping(value = "send/{offerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void send(@PathVariable(value = "offerId") Long offerId, HttpServletRequest request) {
        Offer offer = offerService.findOne(offerId);
        if (offer != null) {
            LOG.info("CREATING OFFER TOKEN...");
            String token = UUID.randomUUID().toString();
            OfferVerificationToken offerVerificationToken = new OfferVerificationToken();
            offerVerificationToken.setOffer(offer);
            offerVerificationToken.setToken(token);
            offerVerificationTokenService.save(offerVerificationToken);

            CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
            LOG.info("SENDING TOKEN TO CUSTOMER...");
            String subject = "عرض رقم " + " ( " + offer.getCode() + " ) ";
            List<String> emails = Lists.newArrayList(offer.getCustomer().getEmail());
            String title = options.getReportTitle();
            String subTitle = options.getReportSubTitle();
            String body = "يسعدنا أن نقدم لكم عرض خاص بمنتجانا، ونأمل أن ينال اعجابكم واستحسان حضراتكم";
            String baseUrl = String.format("%s://%s:%d/", request.getScheme(), request.getServerName(), request.getServerPort());
            String buttonLink = baseUrl + "api/offer/view?offerId=" + offerId + "&token=" + token;
            String buttonText = "تحميل العرض";
            quickEmail.send(subject, emails, title, subTitle, body, buttonLink, buttonText);
            LOG.info(buttonLink);

            LOG.info("UPDATE OFFER STATE TO SENT");
            offer.setCondition(OfferCondition.Sent);
            offerService.save(offer);
        } else {
            throw new CustomException("عفواً، لا يوجد هذا العرض بقاعدة البيانات");
        }
    }

    @GetMapping(value = "/view", produces = "application/pdf")
    @ResponseBody
    public void view(@RequestParam(value = "offerId") Long offerId,
                     @RequestParam(value = "token") String token,
                     HttpServletResponse response) throws Exception {
        Offer offer = offerService.findOne(offerId);
        if(offer != null) {
            CompanyOptions options = JSONConverter.toObject(Initializer.company.getOptions(), CompanyOptions.class);
            LOG.info("SKIP VERIFICATION IF TOKEN LENGTH IS ZERO");
            if(options.getTokenLengthInHours() == 0){
                reportExporter.export("عرض سعر", ExportType.PDF, response, asyncOfferGenerator.generate(offerId).get());
            }else{
                LOG.info("CHECK IF TOKEN IS NOT EXPIRED");
                OfferVerificationToken offerVerificationToken = offerVerificationTokenService.findByOfferAndToken(offer, token);
                if(offerVerificationToken != null){
                    DateTime now = new DateTime();
                    if(new DateTime(offerVerificationToken.getModifiedDate())
                            .plusHours(options.getTokenLengthInHours())
                            .isAfter(now)){
                        LOG.info("TOKEN STILL ACCEPTED, SHOW IT TO VISITOR");
                        reportExporter.export("عرض سعر", ExportType.PDF, response, asyncOfferGenerator.generate(offerId).get());
                        offer.setCondition(OfferCondition.Viewed);
                        offerService.save(offer);
                    }else{
                        LOG.info("TOKEN NOT ACCEPTED ANYMORE-1");
                    }
                }else{
                    LOG.info("TOKEN NOT ACCEPTED ANYMORE-2");
                }
            }
        }else{
            LOG.info("OFFER NOT EXIST");
        }
    }

    @GetMapping(value = "filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            //Offer Filters
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
                offerSearch.filter(
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
