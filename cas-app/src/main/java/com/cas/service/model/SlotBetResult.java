package com.cas.service.model;

/**
 * Created by tolga on 13.03.2016.
 */
public class SlotBetResult {

    private double totalWin;
    private int id;
    private int numLineWin;

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

    public int getNumLineWin() {
        return numLineWin;
    }

    public void setNumLineWin(int numLineWin) {
        this.numLineWin = numLineWin;
    }
}
