package com.ayano.atm_server.service;

import com.ayano.atm_server.entity.Card;
import com.ayano.atm_server.entity.Transaction;

import java.math.BigDecimal;

public interface TransferService {

    Transaction createTransfer(Card sender, Card receiver,String note, BigDecimal amount);
    Transaction executeTransfer(String Sender, String Receiver, String note, BigDecimal amount);
}
