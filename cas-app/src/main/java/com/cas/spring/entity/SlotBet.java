package com.cas.spring.entity;

import javax.persistence.*;

/**
 * Created by tolga on 12.03.2016.
 */
@Entity
@Table(name="slot_bets")
public class SlotBet {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column
    double bet;

    @Column
    int lineCount;

    @Column
    double minWin;

    @Column
    boolean winResult;

    @Column
    int numLineWin;

    @Column
    double totalWin;

    @Column
    boolean bonus;

    @Column
    boolean freeSpin;

    @Column
    long update_time;


    @Column
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public double getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(double totalWin) {
        this.totalWin = totalWin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBet() {
        return bet;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public double getMinWin() {
        return minWin;
    }

    public void setMinWin(double minWin) {
        this.minWin = minWin;
    }

    public boolean isWinResult() {
        return winResult;
    }

    public void setWinResult(boolean winResult) {
        this.winResult = winResult;
    }

    public int getNumLineWin() {
        return numLineWin;
    }

    public void setNumLineWin(int numLineWin) {
        this.numLineWin = numLineWin;
    }

    public boolean isBonus() {
        return bonus;
    }

    public void setBonus(boolean bonus) {
        this.bonus = bonus;
    }

    public boolean isFreeSpin() {
        return freeSpin;
    }

    public void setFreeSpin(boolean freeSpin) {
        this.freeSpin = freeSpin;
    }
}
