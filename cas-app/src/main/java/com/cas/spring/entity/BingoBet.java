package com.cas.spring.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 12.05.2016.
 */
@Entity
@Table(name="bingo_bets")
public class BingoBet implements Bet {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column
    private Double bet;

    @Column
    private Double coin;

    @Column
    private boolean win;

    @Column
    private Double totalWin;

    @Column
    private String loseCause;

    @Column
    private String username;

    @Column
    long update_time;


    @Override
    public Double getMinWin() {
        return coin * 5;
    }

    @Override
    public void setWinResult(boolean winResult) {
        this.win = winResult;
    }

    @Override
    public void setLoseCause(String cause) {
        this.loseCause = cause;
    }

    @Override
    public Boolean hasFreeSpin() {
        return false;
    }

    @Override
    public Boolean hasBonus() {
        return false;
    }

    public Double getBet() {
        return bet;
    }

    public void setBet(Double bet) {
        this.bet = bet;
    }

    public Double getCoin() {
        return coin;
    }

    public void setCoin(Double coin) {
        this.coin = coin;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public Double getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(Double totalWin) {
        this.totalWin = totalWin;
    }

    public String getLoseCause() {
        return loseCause;
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

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }
}
