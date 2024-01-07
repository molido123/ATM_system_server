package com.ayano.atm_server.service;

import com.ayano.atm_server.entity.Card;
import com.ayano.atm_server.entity.Transaction;

import java.math.BigDecimal;

public interface WithdrawalService {
    Transaction executeWithdrawal(String CardNumber, String note, BigDecimal amount);

    BigDecimal calculateWithdrawal(BigDecimal balance, BigDecimal amount);

    Transaction createWithdrawal(Card card, String note, BigDecimal amount);

}
