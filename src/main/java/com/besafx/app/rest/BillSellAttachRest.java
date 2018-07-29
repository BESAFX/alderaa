package com.besafx.app.rest;

import com.besafx.app.config.DropboxManager;
import com.besafx.app.entity.Attach;
import com.besafx.app.entity.BillSell;
import com.besafx.app.entity.BillSellAttach;
import com.besafx.app.service.AttachService;
import com.besafx.app.service.BillSellAttachService;
import com.besafx.app.service.BillSellService;
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
@RequestMapping(value = "/api/billSellAttach/")
public class BillSellAttachRest {

    private final static Logger log = LoggerFactory.getLogger(BillSellAttachRest.class);

    public static final String FILTER_TABLE = "" +
            "id," +
            "attach[**,person[id,contact[id,shortName]]]," +
            "billSell[id]";

    @Autowired
    private PersonService personService;

    @Autowired
    private BillSellService billSellService;

    @Autowired
    private BillSellAttachService billSellAttachService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private DropboxManager dropboxManager;

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_CREATE')")
    public String upload(
            @RequestParam(value = "billSellId") Long billSellId,
            @RequestParam(value = "files") MultipartFile[] files,
            Principal principal) throws Exception {

        List<BillSellAttach> billSellAttaches = new ArrayList<>();
        BillSell billSell = billSellService.findOne(billSellId);

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

            String path = "/Shield/BillSell_Attaches/" + billSellId + "/" + attach.getName() + "." + attach.getMimeType();

            Future<Boolean> uploadBillSell = dropboxManager.uploadFile(file, path);
            if (uploadBillSell.get()) {
                Future<String> shareBillSell = dropboxManager.shareFile(path);
                attach.setLink(shareBillSell.get());
                attach = attachService.save(attach);

                BillSellAttach billSellAttach = new BillSellAttach();
                billSellAttach.setAttach(attach);
                billSellAttach.setBillSell(billSell);
                billSellAttaches.add(billSellAttachService.save(billSellAttach));

                notificationService.notifyOne(Notification.builder()
                                                          .message("تم رفع الملف بنجاح")
                                                          .type(NotificationDegree.success)
                                                          .build(), principal.getName());
            }
        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSellAttaches);
    }

    @DeleteMapping(value = "delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DELETE')")
    public Boolean delete(@PathVariable Long id, Principal principal) throws ExecutionException, InterruptedException {
        BillSellAttach billSellAttach = billSellAttachService.findOne(id);
        if (billSellAttach != null) {

            String path = "/Shield/BillSell_Attaches/" +
                    billSellAttach.getBillSell().getId() +
                    "/" + billSellAttach.getAttach().getName() +
                    "." +
                    billSellAttach.getAttach().getMimeType();

            Future<Boolean> deleteBillSell = dropboxManager.deleteFile(path);
            if (deleteBillSell.get()) {
                billSellAttachService.delete(billSellAttach);
                attachService.delete(billSellAttach.getAttach());
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
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DELETE')")
    public void deleteWhatever(@PathVariable Long id, Principal principal) {
        BillSellAttach billSellAttach = billSellAttachService.findOne(id);
        if (billSellAttach != null) {
            billSellAttachService.delete(billSellAttach);
            attachService.delete(billSellAttach.getAttach());
            notificationService.notifyOne(Notification.builder()
                                                      .message("تم حذف الملف بنجاح")
                                                      .type(NotificationDegree.success).build(), principal.getName());
        }
    }

    @GetMapping(value = "findByBillSell/{billSellId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByBillSell(@PathVariable(value = "billSellId") Long billSellId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSellAttachService.findByBillSellId(billSellId));
    }
}
