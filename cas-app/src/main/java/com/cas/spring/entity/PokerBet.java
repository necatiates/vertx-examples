package com.cas.spring.entity;

import javax.persistence.*;

/**
 * Created by tolga on 26.03.2016.
 */
@Entity
@Table(name="poker_bets")
public class PokerBet implements Bet{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column
    double bet;

    @Column
    String username;

    @Column
    int creditIndex;

    @Column
    boolean winResult;

    @Column
    double totalWin;

    @Column
    long update_time;

    @Column
    String loseCause;

    public double getBet() {
        return bet;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCreditIndex() {
        return creditIndex;
    }

    public void setCreditIndex(int creditIndex) {
        this.creditIndex = creditIndex;
    }

    @Override
    public Double getMinWin() {
        return bet * (creditIndex + 1);
    }

    public boolean isWinResult() {
        return winResult;
    }

    @Override
    public void setWinResult(boolean winResult) {
        this.winResult = winResult;
    }

    public double getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(double totalWin) {
        this.totalWin = totalWin;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public String getLoseCause() {
        return loseCause;
    }

    @Override
    public void setLoseCause(String loseCause) {
        this.loseCause = loseCause;
    }
}
