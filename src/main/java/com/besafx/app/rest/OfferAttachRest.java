package com.besafx.app.rest;

import com.besafx.app.config.DropboxManager;
import com.besafx.app.entity.Attach;
import com.besafx.app.entity.Offer;
import com.besafx.app.entity.OfferAttach;
import com.besafx.app.service.AttachService;
import com.besafx.app.service.OfferAttachService;
import com.besafx.app.service.OfferService;
import com.besafx.app.service.PersonService;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationDegree;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "/api/offerAttach/")
public class OfferAttachRest {

    private final static Logger log = LoggerFactory.getLogger(OfferAttachRest.class);

    public static final String FILTER_TABLE = "" +
            "id," +
            "attach[**,person[id,contact[id,shortName]]]," +
            "offer[id]";

    @Autowired
    private PersonService personService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferAttachService offerAttachService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private DropboxManager dropboxManager;

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OFFER_CREATE')")
    public String upload(
            @RequestParam(value = "offerId") Long offerId,
            @RequestParam(value = "files") MultipartFile[] files,
            Principal principal) throws Exception {

        List<OfferAttach> offerAttaches = new ArrayList<>();
        Offer offer = offerService.findOne(offerId);

        ListIterator<MultipartFile> multipartFileListIterator = Lists.newArrayList(files).listIterator();
        while (multipartFileListIterator.hasNext()) {
            MultipartFile file = multipartFileListIterator.next();
            Attach attach = new Attach();
            attach.setName(FilenameUtils.removeExtension(file.getOriginalFilename()));
            attach.setMimeType(FilenameUtils.getExtension(file.getOriginalFilename()));
            attach.setDescription("");
            attach.setSize(file.getSize());
            attach.setDate(new DateTime().toDate());
            attach.setPerson(personService.findByEmail(principal.getName()));

            String path = "/Alderaa/Offer_Attaches/" + offerId + "/" + attach.getName() + "." + attach.getMimeType();

            Future<Boolean> uploadOffer = dropboxManager.uploadFile(file, path);
            if (uploadOffer.get()) {
                Future<String> shareOffer = dropboxManager.shareFile(path);
                attach.setLink(shareOffer.get());
                attach = attachService.save(attach);

                OfferAttach offerAttach = new OfferAttach();
                offerAttach.setAttach(attach);
                offerAttach.setOffer(offer);
                offerAttaches.add(offerAttachService.save(offerAttach));

                notificationService.notifyOne(Notification.builder()
                                                          .message("تم رفع الملف بنجاح")
                                                          .type(NotificationDegree.success)
                                                          .build(), principal.getName());
            }
        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), offerAttaches);
    }

    @DeleteMapping(value = "delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OFFER_DELETE')")
    public Boolean delete(@PathVariable Long id, Principal principal) throws ExecutionException, InterruptedException {
        OfferAttach offerAttach = offerAttachService.findOne(id);
        if (offerAttach != null) {

            String path = "/Alderaa/Offer_Attaches/" +
                    offerAttach.getOffer().getId() +
                    "/" + offerAttach.getAttach().getName() +
                    "." +
                    offerAttach.getAttach().getMimeType();

            Future<Boolean> deleteOffer = dropboxManager.deleteFile(path);
            if (deleteOffer.get()) {
                offerAttachService.delete(offerAttach);
                attachService.delete(offerAttach.getAttach());
                notificationService.notifyOne(Notification.builder()
                                                          .message("تم حذف الملف بنجاح")
                                                          .type(NotificationDegree.error).build(), principal.getName());
                return true;
            } else {
                notificationService.notifyOne(Notification.builder()
                                                          .message("لا يمكن حذف الملف حيث يبدو غير موجود")
                                                          .type(NotificationDegree.error).build(), principal.getName());
                return false;
            }
        } else {
            return false;
        }
    }

    @DeleteMapping(value = "deleteWhatever/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OFFER_DELETE')")
    public void deleteWhatever(@PathVariable Long id, Principal principal) {
        OfferAttach offerAttach = offerAttachService.findOne(id);
        if (offerAttach != null) {
            offerAttachService.delete(offerAttach);
            attachService.delete(offerAttach.getAttach());
            notificationService.notifyOne(Notification.builder()
                                                      .message("تم حذف الملف بنجاح")
                                                      .type(NotificationDegree.success).build(), principal.getName());
        }
    }

    @GetMapping(value = "findByOffer/{offerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByOffer(@PathVariable(value = "offerId") Long offerId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), offerAttachService.findByOfferId(offerId));
    }
}
