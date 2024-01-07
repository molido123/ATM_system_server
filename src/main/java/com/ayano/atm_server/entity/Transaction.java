package com.ayano.atm_server.entity;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Transaction {
    private String doer;
    private String trackingCode;
    private String sender;
    private String receiver;
    private String note;
    private String dateTime;
    private BigDecimal amount;
    private String transactionType;

    public Transaction(String doer, String trackingCode, String sender, String receiver, String note, String dateTime, BigDecimal amount, String transactionType) {
        this.doer = doer;
        this.trackingCode = trackingCode;
        this.sender = sender;
        this.receiver = receiver;
        this.note = note;
        this.dateTime = dateTime;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public void setDoer(String doer) {
        this.doer = doer;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "交易{" +
                "执行者='" + doer + '\'' +
                ", 交易码='" + trackingCode + '\'' +
                ", 发送者='" + sender + '\'' +
                ", 接收者='" + receiver + '\'' +
                ", 备注='" + note + '\'' +
                ", 时间='" + dateTime + '\'' +
                ", 数额=" + amount +
                ", 交易类型='" + transactionType + '\'' +
                '}';
    }
}