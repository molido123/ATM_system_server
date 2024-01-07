package com.ayano.atm_server.entity;

import java.math.BigDecimal;

public class Card {
    private String holder;
    private String cardNumber;
    private String password;
    private BigDecimal balance;

    public Card(String holder, String cardNumber, String password, BigDecimal balance) {
        this.holder = holder;
        this.cardNumber = cardNumber;
        this.password = password;
        this.balance = balance;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "银行卡信息{" +
                "持有者='" + holder + '\'' +
                ", 卡号'" + cardNumber + '\'' +
                ", 密码='" + password + '\'' +
                ", 余额=" + balance +
                '}';
    }
}
