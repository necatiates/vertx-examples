package com.cas.spring.entity;

import javax.persistence.*;

/**
 * Created by tolga on 09.04.2016.
 */
@Entity
public class CreditCardDeposit {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;


    private String cardFullName;
    private String expiryDate;
    private String cvc;
    private String cardNo;


    @Column
    private double amount;

    @Column
    private boolean processed;

    @Column
    private String username;


    public String getCardFullName() {
        return cardFullName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setCardFullName(String cardFullName) {
        this.cardFullName = cardFullName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
