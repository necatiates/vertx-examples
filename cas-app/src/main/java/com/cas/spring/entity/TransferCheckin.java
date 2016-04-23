package com.cas.spring.entity;

import javax.persistence.*;

/**
 * Created by tolga on 09.04.2016.
 */
@Entity
public class TransferCheckin {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column
    private String  bank;

    @Column
    private String name_surname;

    @Column
    private String tcno;

    @Column
    private double amount;

    @Column
    private boolean processed;

    @Column
    private String username;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getName_surname() {
        return name_surname;
    }


    public String getTcno() {
        return tcno;
    }

    public void setTcno(String tcno) {
        this.tcno = tcno;
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
}
