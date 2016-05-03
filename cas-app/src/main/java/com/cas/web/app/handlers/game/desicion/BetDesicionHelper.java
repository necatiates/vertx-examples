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
    public static JsonObject invoke(Bet Bet,Session em) {
        JsonObject response = new JsonObject();
        Cash slotCash = (Cash) em.get(Cash.class,"Slots");
        Cash cardsCash = (Cash) em.get(Cash.class,"Cards");
        Cash strachCash = (Cash) em.get(Cash.class,"Strach");

        /*
            Decide to win or not
        */

        double totalCapital = slotCash.getCapital() + cardsCash.getCapital() + strachCash.getCapital();
        double totalCash = slotCash.getCash() + cardsCash.getCash() + strachCash.getCash();

        Double avaliableCash = ((totalCapital * 0.20 + totalCash)  / 2 ) / 5;
        if(Bet.getMinWin() <= avaliableCash ){
            double genRandom = randomBoolean();

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
            }else if(isBonus){
                Bet.setLoseCause("Bonus");
            }else if(isFreeSpin){
                Bet.setLoseCause("FreeSpin");
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