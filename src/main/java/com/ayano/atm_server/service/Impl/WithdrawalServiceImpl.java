package com.ayano.atm_server.service.Impl;

import com.ayano.atm_server.DAO.CardDatabase;
import com.ayano.atm_server.entity.*;
import com.ayano.atm_server.service.TransactionService;
import com.ayano.atm_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

import static com.ayano.atm_server.utils.TextUtils.getCurrentDateTime;

@Service
public class WithdrawalServiceImpl implements com.ayano.atm_server.service.WithdrawalService {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private CardDatabase cardDatabase;
    @Override
    public Transaction executeWithdrawal(String cardNumber, String note, BigDecimal amount) {
        Card card = cardDatabase.getCardByCardNumber(cardNumber);
        Transaction transaction= createWithdrawal(card, note, amount);
        BigDecimal balance = calculateWithdrawal(card.getBalance(), amount);
        if(balance.compareTo(new BigDecimal(0)) < 0){
            throw new RuntimeException("您的余额不足");
        }
        if (transactionService.updateCardBalance(card, balance) && transactionService.insertTransaction(transaction)) {
            transactionService.updateUserTransactionsList(userService.getUserByUsername(card.getHolder()),transaction);
            return transaction;
        }
        return null;
    }
    @Override
    public BigDecimal calculateWithdrawal(BigDecimal balance, BigDecimal amount) {
        return balance.subtract(amount);
    }

    @Override
    public Transaction createWithdrawal(Card card, String note, BigDecimal amount) {
        String doer = card.getHolder();
        String trackingCode = transactionService.generateTrackingCode();
        String sender = card.getCardNumber();
        String dateTime = getCurrentDateTime();
        String transactionType = "取款";
        return new Transaction(doer, trackingCode, sender, "无", note, dateTime, amount, transactionType);
    }

}

