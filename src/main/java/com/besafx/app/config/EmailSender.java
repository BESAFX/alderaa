package com.besafx.app.config;

import com.besafx.app.entity.Company;
import com.besafx.app.init.Initializer;
import com.besafx.app.util.CompanyOptions;
import com.besafx.app.util.JSONConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.concurrent.Future;

@Service
public class EmailSender {

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";

    private static final int SMTP_HOST_PORT = 587;

    private static String SMTP_AUTH_USER = "shieldsender@gmail.com";

    private static String SMTP_AUTH_PWD = "147896325Zxc";

    private final Logger log = LoggerFactory.getLogger(EmailSender.class);

    private Session mailSession;

    private Transport transport;

    private MimeMessage message;

    public void init() {
        log.info("PREPARING EMAIL SERVICE...");
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");
        props.put("mail.transport.protocol", "smtps");
        mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        log.info("PREPARING EMAIL SERVICE SUCCESSFULLY");
    }

    @Async("threadSinglePool")
    public Future<Boolean> send(String title, String content, List<String> toEmailList) {
        try {
            log.info("SLEEPING FOR 10 SECONDS");
            Thread.sleep(10000);
            log.info("TRYING SENDING EMAIL TO THIS DESTINATIONS: " + toEmailList);
            transport = mailSession.getTransport("smtp");
            message = new MimeMessage(mailSession);
            message.setSubject(title, "UTF-8");
            message.setText(content, "UTF-8", "html");
            message.setFrom(new InternetAddress(Initializer.company.getEmail(), Initializer.company.getName(), "UTF-8"));
            toEmailList.stream().distinct().forEach(email -> {
                try {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            log.info("SENDING EMAIL SUCCESSFULLY TO THIS DESTINATIONS: " + toEmailList);
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            send(title, content, toEmailList);
            return new AsyncResult<>(false);
        }
    }

    @Async("threadSinglePool")
    public Future<Boolean> send(String title, String content, List<String> toEmailList, List<File> files) {
        try {
            log.info("SLEEPING FOR 10 SECONDS");
            Thread.sleep(10000);
            log.info("TRYING SENDING EMAIL TO THIS DESTINATIONS: " + toEmailList);
            transport = mailSession.getTransport("smtp");
            message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(Initializer.company.getEmail(), Initializer.company.getName(), "UTF-8"));
            message.setSubject(title, "UTF-8");
            toEmailList.stream().distinct().forEach(email -> {
                try {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html; charset=UTF-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            ListIterator<File> fileListIterator = files.listIterator();
            while (fileListIterator.hasNext()) {
                File file = fileListIterator.next();
                messageBodyPart = new MimeBodyPart();
                FileDataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file.getName());
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            log.info("SENDING EMAIL SUCCESSFULLY TO THIS DESTINATIONS: " + toEmailList);
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            send(title, content, toEmailList, files);
            return new AsyncResult<>(false);
        }
    }

    @Async("threadSinglePool")
    public Future<Boolean> send(String title, String content, String email) {
        try {
            log.info("SLEEPING FOR 10 SECONDS");
            Thread.sleep(10000);
            log.info("TRYING SENDING EMAIL TO THIS DESTINATIONS: " + email);
            transport = mailSession.getTransport("smtp");
            message = new MimeMessage(mailSession);
            message.setSubject(title, "UTF-8");
            message.setText(content, "UTF-8", "html");
            message.setFrom(new InternetAddress(Initializer.company.getEmail(), Initializer.company.getName(), "UTF-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            log.info("SENDING EMAIL SUCCESSFULLY TO THIS DESTINATIONS: " + email);
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new AsyncResult<>(false);
        }
    }

    @Async("threadSinglePool")
    public Future<Boolean> send(String title, String content, String email, List<File> files) {
        try {
            log.info("Sleeping for 10 seconds");
            Thread.sleep(10000);
            log.info("TRYING SENDING EMAIL TO THIS DESTINATIONS: " + email);
            transport = mailSession.getTransport("smtp");
            message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(Initializer.company.getEmail(), Initializer.company.getName(), "UTF-8"));
            message.setSubject(title, "UTF-8");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html; charset=UTF-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            ListIterator<File> fileListIterator = files.listIterator();
            while (fileListIterator.hasNext()) {
                File file = fileListIterator.next();
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file.getName());
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            log.info("SENDING EMAIL SUCCESSFULLY TO THIS DESTINATIONS: " + email);
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            send(title, content, email, files);
            return new AsyncResult<>(false);
        }
    }
}
