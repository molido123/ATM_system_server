package com.ayano.atm_server.service.Impl;
import com.ayano.atm_server.DAO.*;
import com.ayano.atm_server.entity.*;
import com.ayano.atm_server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import static com.ayano.atm_server.utils.TextUtils.getCurrentDateTime;

@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    private  CardDatabase cardDatabase;
    @Autowired
    private DepositService depositService;
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Override
    public Transaction createTransfer(Card sender, Card receiver,String note,BigDecimal amount){
        String doer = sender.getHolder();
        String trackingCode = transactionService.generateTrackingCode();
        String dateTime = getCurrentDateTime();
        String transactionType = "转账";
        return new Transaction(doer, trackingCode, sender.getCardNumber(), receiver.getCardNumber(), note, dateTime, amount, transactionType);
    }
    @Override
    public Transaction executeTransfer(String Sender, String Receiver, String note, BigDecimal amount) {
        withdrawalService.executeWithdrawal(Sender ,note,amount);
        depositService.executeDeposit(Receiver,note,amount);
        Card card_sender = cardDatabase.getCardByCardNumber(Sender);
        Card card_receiver = cardDatabase.getCardByCardNumber(Receiver);
        Transaction transaction=createTransfer(card_sender,card_receiver,note,amount);
        transactionService.insertTransaction(transaction);
        transactionService.updateUserTransactionsList(userService.getUserByUsername(card_sender.getHolder()),transaction);
        return transaction;
    }
    private Card getCardByCardNumber(String cardNumber) {
//        try {
            return cardDatabase.getCardByCardNumber(cardNumber);
//        } catch (SQLException exception) {
//            return null;
//        }
    }

}
