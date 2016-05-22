package com.cas.web.app.handlers.game.desicion;

import com.cas.StaticDefinitions;
import com.cas.spring.entity.Bet;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.GlobalSettings;
import com.cas.spring.entity.SlotBet;
import com.cas.web.app.Server;
import io.vertx.core.json.JsonObject;
import org.hibernate.Session;

/**
 * Created by tolga on 26.03.2016.
 */
public class BetDesicionHelper {
    public static JsonObject invoke(Bet Bet,Session em) {
        JsonObject response = new JsonObject();
        Cash gameCash = (Cash) em.get(Cash.class, StaticDefinitions.GAME_CASH_NAME);
        Cash bonusCash = (Cash) em.get(Cash.class,StaticDefinitions.BONUS_CASH_NAME);
        GlobalSettings absoluteValue = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.ABSOLUTE_STOP_VALUE);

        /*
            If cash is lower than absolute value
            reject the game player loses

         */

         if(gameCash.getCash() <= Double.parseDouble(absoluteValue.getValue())){
             Bet.setWinResult(false);
             Bet.setLoseCause("AbsoluteLose");
             response.put("freeSpin",false);
             response.put("bonus",false);
             response.put("_win_",false);
             response.put("_maxWin_", 0);
             return response;
         }


        /*
            Decide to win or not
        */

        if(Bet.getMinWin() <= gameCash.getCash()){
            double genRandom = randomBoolean();

            GlobalSettings winPerc = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.WIN_PERCENTAGE_SETTINGS_NAME);
            boolean isWin = genRandom <= Double.parseDouble(winPerc.getValue());
            Bet.setWinResult(isWin);

            boolean isFreeSpin;
            if(Bet instanceof SlotBet && ((SlotBet) Bet).getCurFreeSpinCnt() > 0){
                isFreeSpin = false;
            }else {
                if (Bet.hasFreeSpin() && !isWin) {
                    double genRandomFreeSpin = randomBoolean();
                    GlobalSettings freeSpinPercentage = (GlobalSettings) em.get(GlobalSettings.class, StaticDefinitions.FREE_SPIN_SETTINGS_NAME);
                    isFreeSpin = genRandomFreeSpin <= Double.parseDouble(freeSpinPercentage.getValue());
                } else {
                    isFreeSpin = false;
                }
            }

            boolean isBonus;
            if(Bet.hasBonus() && bonusCash.getCash() > 0 && !isWin && !isFreeSpin){
                double genRandomBonus = randomBoolean();
                GlobalSettings bonusPercentage = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.BONUS_SETTINGS_NAME);
                isBonus = genRandomBonus <= Double.parseDouble(bonusPercentage.getValue());
            }else {
                isBonus = false;
            }

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
            if(isBonus){
                GlobalSettings bonusGiveAwaySettings = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.BONUS_GIVEAWAY_PERCENTAGE_SETTINGS);
                response.put("maxBonus",bonusCash.getCash() * (Double.parseDouble(bonusGiveAwaySettings.getValue()) / 100));
            }
            response.put("_win_",isWin);
            if(isWin) {
                GlobalSettings cashGiveAwayPerc = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.CASH_GIVEAWAY_PERCENTAGE);
                response.put("_maxWin_", gameCash.getCash() * (Double.parseDouble(cashGiveAwayPerc.getValue()) / 100 ));
            }else{
                response.put("_maxWin_", 0);
            }
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