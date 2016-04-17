package com.cas.spring.entity;

import javax.persistence.*;

/**
 * Created by tolga on 12.03.2016.
 */
@Entity
@Table(name="slot_bets")
public class BlackJackBet {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column
    double bet;

    @Column
    boolean winResult;

    @Column
    double totalWin;

    @Column
    long update_time;

    @Column
    double minWin;


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
}
