package com.besafx.app;

import com.besafx.app.config.DropboxManager;
import com.besafx.app.config.EmailSender;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.service.BankTransactionService;
import com.besafx.app.util.JSONConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTests {

    private final static Logger log = LoggerFactory.getLogger(MainTests.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private BankTransactionService bankTransactionService;

    long i = 1;

    @Test
    public void contextLoads() throws Exception {
        log.info("Start Initializing From Main...");
        context.getBean(EmailSender.class).init();
        context.getBean(DropboxManager.class).init();
        context.getBean(JSONConverter.class).init();


        bankTransactionService.findAll().forEach(bankTransaction -> {
            bankTransaction.setCode(i);
            i++;
            bankTransaction.setPaymentMethod(PaymentMethod.Cash);
            bankTransactionService.save(bankTransaction);
        });

        Thread.sleep(20000000);
    }


}
