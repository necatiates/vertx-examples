package com.cas.spring.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.text.SimpleDateFormat;

/**
 * Created by tolga on 12.03.2016.
 */
@Entity
@Table(name="slot_bets")
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class SlotBet implements Bet{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column
    double bet;

    @Column
    Integer lineCount;

    @Column
    double minWin;

    @Column
    boolean winResult;

    @Column
    Integer numLineWin;

    @Column
    double totalWin;

    @Column
    Boolean bonus;

    @Column
    Boolean freeSpin;

    @Column
    long update_time;

    @Column
    String loseCause;

    @Column
    String username;

    @Column
    String gameName;

    @Column
    int curFreeSpinCnt;

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
        if(lineCount == null){
            return 0;
        }
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public Double getMinWin() {
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
        if(numLineWin == null){
            return 0;
        }
        return numLineWin;
    }

    public void setNumLineWin(int numLineWin) {
        this.numLineWin = numLineWin;
    }

    public boolean isBonus() {
        if(bonus == null){
            return false;
        }
        return bonus;
    }

    public void setBonus(boolean bonus) {
        this.bonus = bonus;
    }

    public boolean isFreeSpin() {
        if(freeSpin == null){
            return false;
        }
        return freeSpin;
    }

    public void setFreeSpin(boolean freeSpin) {
        this.freeSpin = freeSpin;
    }

    public String getLoseCause() {
        return loseCause;
    }

    public void setLoseCause(String loseCause) {
        this.loseCause = loseCause;
    }

    @Override
    public Boolean hasFreeSpin() {
        return isFreeSpin();
    }

    @Override
    public Boolean hasBonus() {
        return isBonus();
    }

    @JsonProperty("formattedDate")
    public String getUppercaseUsername() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dt.format(update_time);
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getCurFreeSpinCnt() {
        return curFreeSpinCnt;
    }

    public void setCurFreeSpinCnt(int curFreeSpinCnt) {
        this.curFreeSpinCnt = curFreeSpinCnt;
    }
}
