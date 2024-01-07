package com.ayano.atm_server.service.Impl;
import com.ayano.atm_server.DAO.CardDatabase;
import com.ayano.atm_server.DAO.TransactionDatabase;
import com.ayano.atm_server.DAO.UserDatabase;
import com.ayano.atm_server.entity.Transaction;
import com.ayano.atm_server.entity.*;
import com.ayano.atm_server.service.TransactionService;
import com.ayano.atm_server.service.UserService;
import com.ayano.atm_server.utils.PasswordUtils;
import com.ayano.atm_server.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private CardDatabase cardDatabase;
    @Autowired
    private TransactionDatabase transactionDatabase;
    @Autowired
    private UserService userService;
    // CARD SECTION
    @Override
    public boolean updateCardBalance(Card card, BigDecimal balance) {
//        try {
            card.setBalance(balance);
            return cardDatabase.updateCard(card);
//        } catch (SQLException exception) {
//            return false;
//        }
    }
    @Override
    public void updateUserTransactionsList(User user, Transaction transaction) {
        user.getTransactions().add(transaction);
    }
    @Override
    public boolean validateCardNumber(String cardNumber) {
//        try {
            Card card = cardDatabase.getCardByCardNumber(cardNumber);
            return card != null;
//        } catch (SQLException exception) {
//            return false;
//        }
    }
    @Override
    public boolean validateCardPassword(String password, User user) {
        return PasswordUtils.decodePassword(user.getCard().getPassword(), user.getCard().getCardNumber()).equals(password);
    }
    @Override
    public boolean validateCardNumberPattern(String text) {
        return text.matches("\\d*") && text.length() == 16;
    }

    // TRANSACTION SECTION
    @Override
    public boolean insertTransaction(Transaction transaction) {
//        try {
            return transactionDatabase.insertTransaction(transaction);
//        } catch (SQLException exception) {
//            return false;
//        }
    }

    @Override
    public String generateTrackingCode() {
        String trackingCode;
        do {
            trackingCode = TextUtils.random(6, false, true);
        } while (!isNewTrackingCode(trackingCode));
        return trackingCode;
    }
    @Override
    public boolean isNewTrackingCode(String trackingCode) {
//        try {
            List<Transaction> transactions = transactionDatabase.getAllTransactions();
            for (Transaction transaction : transactions) {
                if (transaction.getTrackingCode().equals(trackingCode)) {
                    return false;
                }
            }
            return true;
//        } catch (SQLException exception) {
//            return false;
//        }
    }
    @Override
    public boolean validateAmountPattern(String text) {
        return text.matches("\\d*") && !text.startsWith("0") && !text.isEmpty() && Integer.parseInt(text) % 100 == 0;
    }
    @Override
    public boolean isHolder(String cardNumber, String userName){
        User user=userService.getUserByUsername(userName);
        return Objects.equals(userName, user.getUsername());
    }
}
