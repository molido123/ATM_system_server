
package com.ayano.atm_server.service.Impl;

import com.ayano.atm_server.DAO.CardDatabase;
import com.ayano.atm_server.entity.*;
import com.ayano.atm_server.service.DepositService;
import com.ayano.atm_server.service.TransactionService;
import com.ayano.atm_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.ayano.atm_server.utils.TextUtils.getCurrentDateTime;

@Service
public class DepositServiceImpl implements DepositService {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private CardDatabase cardDatabase;
    @Override
    public Transaction executeDeposit(String cardNumber, String note, BigDecimal amount) {
        Card card = cardDatabase.getCardByCardNumber(cardNumber);
        Transaction transaction=createDeposit(card, note, amount);
        BigDecimal balance = calculateDeposit(card.getBalance(), amount);
        if (transactionService.updateCardBalance(card, balance) && transactionService.insertTransaction(transaction)) {
            transactionService.updateUserTransactionsList(userService.getUserByUsername(card.getHolder()),transaction);
            return transaction;
        }
        return null;
    }
    @Override
    public Transaction createDeposit(Card Card, String note, BigDecimal amount) {
        String doer = Card.getHolder();
        String trackingCode = transactionService.generateTrackingCode();
        String sender = Card.getCardNumber();
        String receiver = "-";
        String dateTime = getCurrentDateTime();
        String transactionType = "存款";

        return new Transaction(doer, trackingCode, sender, receiver, note, dateTime, amount, transactionType);
    }
    @Override
    public BigDecimal calculateDeposit(BigDecimal balance, BigDecimal amount) {
        return balance.add(amount);
    }

    @Override
    public String getDepositInfo(Transaction transaction) {
        return "交易码: " +   transaction.getTrackingCode();
    }
}
