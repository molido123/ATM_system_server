package com.ayano.atm_server.service;

import com.ayano.atm_server.entity.Card;
import com.ayano.atm_server.entity.Transaction;
import com.ayano.atm_server.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public interface TransactionService {

    boolean updateCardBalance(Card card, BigDecimal balance);
    void updateUserTransactionsList(User user, Transaction transaction);
    boolean validateCardNumber(String cardNumber);
    boolean validateCardPassword(String password, User user);
    boolean validateCardNumberPattern(String text);
    boolean insertTransaction(Transaction transaction);
    String generateTrackingCode();
    boolean isNewTrackingCode(String trackingCode);
    boolean validateAmountPattern(String text);

    boolean isHolder(String cardNumber, String userName);
}
