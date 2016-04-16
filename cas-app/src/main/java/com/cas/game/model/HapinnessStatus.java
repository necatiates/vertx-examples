package com.cas.game.model;

/**
 * Created by tolga on 20.03.2016.
 */
public class HapinnessStatus {
    private String status = "NEUTRAL";
    private int loseGameCount = 0;
    private int winGameCount = 0;
    private double totalWinLoss = 0.0;
    private double winPerc = 0.0;
    private double sessionStartMoney;

    public HapinnessStatus(double sessionStartMoney){
        this.sessionStartMoney = sessionStartMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLoseGameCount() {
        return loseGameCount;
    }

    public void setLoseGameCount(int loseGameCount) {
        this.loseGameCount = loseGameCount;
        checkHapinness();
    }

    private void checkHapinness() {
        if(loseGameCount > winGameCount){
            setStatus("UNHAPPY");
        }else if(winGameCount > loseGameCount){
            setStatus("HAPPY");
        }else {
            setStatus("NEUTRAL");
        }
    }

    public int getWinGameCount() {
        return winGameCount;
    }

    public void setWinGameCount(int winGameCount) {
        this.winGameCount = winGameCount;
        checkHapinness();
    }

    public double getTotalWinLoss() {
        return totalWinLoss;
    }

    public void setTotalWinLoss(double totalWinLoss) {
        this.totalWinLoss = totalWinLoss;
    }

    public double getWinPerc() {
        return winPerc;
    }

    public void setWinPerc(double winPerc) {
        this.winPerc = winPerc;
    }

    public void addGameResult(double winLossAmount){
        this.totalWinLoss = this.totalWinLoss + winLossAmount;
        setWinPerc(this.totalWinLoss / sessionStartMoney * 100);
        if(winLossAmount > 0){
            setWinGameCount(getWinGameCount() + 1);
        }else if(winLossAmount <= 0){
            setLoseGameCount(getLoseGameCount() + 1);
        }
    }
}
