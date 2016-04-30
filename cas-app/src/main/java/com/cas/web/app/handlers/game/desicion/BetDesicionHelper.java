package com.cas.web.app.handlers.game.desicion;

import com.cas.spring.entity.Bet;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.GlobalSettings;
import com.cas.web.app.Server;
import io.vertx.core.json.JsonObject;
import org.hibernate.Session;

/**
 * Created by tolga on 26.03.2016.
 */
public class BetDesicionHelper {
    public static JsonObject invoke(Bet Bet, Cash cash) {
        JsonObject response = new JsonObject();

        /*
            Decide to win or not
        */
        Double avaliableCash = ((cash.getCapital() * 0.20 + cash.getCash())  / 2 ) / 5;
        if(Bet.getMinWin() <= avaliableCash ){
            double genRandom = randomBoolean();

            Session em = Server.factory.openSession();
            boolean isBonus;
            if(Bet.hasBonus()){
                double genRandomFreeSpin = randomBoolean();
                GlobalSettings bonusPercentage = (GlobalSettings) em.get(GlobalSettings.class,"BonusPercentage");
                isBonus = genRandomFreeSpin <= Double.parseDouble(bonusPercentage.getValue());
            }else {
                isBonus = false;
            }

            boolean isFreeSpin;
            if(Bet.hasFreeSpin() && !isBonus){
                double genRandomFreeSpin = randomBoolean();
                GlobalSettings freeSpinPercentage = (GlobalSettings) em.get(GlobalSettings.class,"FreeSpinPercentage");
                isFreeSpin = genRandomFreeSpin <= Double.parseDouble(freeSpinPercentage.getValue());
            }else {
                isFreeSpin = false;
            }

            GlobalSettings winPerc = (GlobalSettings) em.get(GlobalSettings.class,"WinPercentage");
            boolean isWin = genRandom <= Double.parseDouble(winPerc.getValue()) && (!isBonus && !isFreeSpin);
            Bet.setWinResult(isWin);

            if(isWin){
                Bet.setLoseCause("RandomWin");
            }else{
                Bet.setLoseCause("RandomLose");
            }
            response.put("freeSpin",isFreeSpin);
            response.put("bonus",isBonus);
            response.put("_win_",isWin);
            response.put("_maxWin_",avaliableCash);
        } else{
            Bet.setWinResult(false);
            response.put("_win_",false);
            response.put("_maxWin_",0);
            Bet.setLoseCause("CashLose");
        }
        return response;
    }
    public static double randomBoolean(){
        return Math.random() * 100;
    }
}