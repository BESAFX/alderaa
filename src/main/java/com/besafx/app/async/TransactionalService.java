package com.besafx.app.async;

import com.besafx.app.entity.Bank;
import com.besafx.app.entity.BankTransaction;
import com.besafx.app.entity.CustomerPayment;
import com.besafx.app.entity.SupplierPayment;
import com.besafx.app.entity.projection.BankTransactionAmount;
import com.besafx.app.init.Initializer;
import com.besafx.app.service.BankService;
import com.besafx.app.service.BankTransactionService;
import com.besafx.app.service.CustomerPaymentService;
import com.besafx.app.service.SupplierPaymentService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.ListIterator;

@Service
public class TransactionalService {

    private final static Logger LOG = LoggerFactory.getLogger(TransactionalService.class);

    @Autowired
    private BankService bankService;

    @Autowired
    private BankTransactionService bankTransactionService;

    @Autowired
    private CustomerPaymentService customerPaymentService;

    @Autowired
    private SupplierPaymentService supplierPaymentService;

    @Transactional
    public List<Bank> readAllBanks() {
        List<Bank> banks = Lists.newArrayList(bankService.findAll());
        ListIterator<Bank> bankListIterator = banks.listIterator();
        while (bankListIterator.hasNext()) {
            Bank bank = bankListIterator.next();

            Double depositAmount = bankTransactionService
                    .findByBankAndTransactionTypeIn(bank, Lists.newArrayList(
                            Initializer.transactionTypeDeposit,
                            Initializer.transactionTypeDepositTransfer),
                                                    BankTransactionAmount.class)
                    .stream().mapToDouble(BankTransactionAmount::getAmount).sum();

            Double withdrawAmount = bankTransactionService
                    .findByBankAndTransactionTypeIn(bank, Lists.newArrayList(
                            Initializer.transactionTypeWithdraw,
                            Initializer.transactionTypeWithdrawTransfer,
                            Initializer.transactionTypeExpense),
                                                    BankTransactionAmount.class)
                    .stream().mapToDouble(BankTransactionAmount::getAmount).sum();

            LOG.info("مجموع الإيداعات = " + depositAmount);
            bank.setTotalDeposits(depositAmount);

            LOG.info("مجموع السحبيات = " + withdrawAmount);
            bank.setTotalWithdraws(withdrawAmount);

            bank.setBalance(depositAmount - withdrawAmount);
        }
        return banks;
    }

    @Transactional
    public Long getNextBankTransactionCode(){
        BankTransaction topBankTransaction = bankTransactionService.findTopByOrderByCodeDesc();
        if (topBankTransaction == null) {
            return Long.valueOf(1);
        } else {
            return topBankTransaction.getCode() + 1;
        }
    }

    @Transactional
    public Integer getNextCustomerPaymentCode(){
        CustomerPayment topCustomerPayment = customerPaymentService.findTopByOrderByCodeDesc();
        if (topCustomerPayment == null) {
            return 1;
        } else {
            return topCustomerPayment.getCode() + 1;
        }
    }

    @Transactional
    public Integer getNextSupplierPaymentCode(){
        SupplierPayment topSupplierPayment = supplierPaymentService.findTopByOrderByCodeDesc();
        if (topSupplierPayment == null) {
            return 1;
        } else {
            return topSupplierPayment.getCode() + 1;
        }
    }
}
