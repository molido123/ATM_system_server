package com.ayano.atm_server.service;

import java.math.BigDecimal;

import com.ayano.atm_server.entity.Card;
import com.ayano.atm_server.entity.Transaction;
import com.ayano.atm_server.entity.User;
public interface DepositService {
    Transaction executeDeposit(String CardNumber, String note, BigDecimal amount);
    Transaction createDeposit(Card Card, String note, BigDecimal amount);

    BigDecimal calculateDeposit(BigDecimal balance, BigDecimal amount);

    String getDepositInfo(Transaction transaction);
}
