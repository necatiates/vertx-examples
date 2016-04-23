package com.cas.web.app.handlers;

import com.cas.cache.CacheManager;
import com.cas.spring.entity.Bet;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.PokerBet;
import io.vertx.core.json.JsonObject;

import java.util.Random;

/**
 * Created by tolga on 26.03.2016.
 */
public class BetDesicionHelper {
    private static Random random = new Random();
    public static JsonObject invoke(Bet Bet, Cash cash) {
        JsonObject response = new JsonObject();
        double ratio = 1;
        /*
            Decide to win or not
        */
        Double avaliableCash = ((cash.getCapital() * 0.20 + cash.getCash())  / 2 ) / 5;
        if(Bet.getMinWin() < avaliableCash && ratio < 1){
            Bet.setWinResult(true);
            response.put("_win_",true);
            response.put("_maxWin_",avaliableCash);
            Bet.setLoseCause("RatioWin");
        }else if(Bet.getMinWin() < avaliableCash && ratio == 1){
            boolean genRandom = randomBoolean();
            Bet.setWinResult(genRandom);
            if(genRandom){
                Bet.setLoseCause("RandomWin");
            }else{
                Bet.setLoseCause("RandomLose");
            }
            response.put("_win_",genRandom);
            response.put("_maxWin_",avaliableCash);
        } else{
            Bet.setWinResult(false);
            response.put("_win_",false);
            response.put("_maxWin_",0);
            Bet.setLoseCause("RatioLose");
        }
        return response;
    }
    public static boolean randomBoolean(){
        return Math.random() < 0.5;
    }
}