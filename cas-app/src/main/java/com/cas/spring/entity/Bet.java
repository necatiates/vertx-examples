package com.cas.spring.entity;

/**
 * Created by tolga on 26.03.2016.
 */
public interface Bet {
    Double getMinWin();
    void  setWinResult(boolean winResult);
    void  setLoseCause(String cause);
}
