package com.cas.web.app.handlers.game.desicion;

import com.cas.StaticDefinitions;
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
        Cash gameCash = (Cash) em.get(Cash.class, StaticDefinitions.GAME_CASH_NAME);
        Cash bonusCash = (Cash) em.get(Cash.class,StaticDefinitions.BONUS_CASH_NAME);
        GlobalSettings bonusGiveAwaySettings = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.BONUS_GIVEAWAY_PERCENTAGE_SETTINGS);
        /*
            Decide to win or not
        */


        if(Bet.getMinWin() <= gameCash.getCash()){
            double genRandom = randomBoolean();

            boolean isBonus;
            if(Bet.hasBonus() && bonusCash.getCash() > 0){
                double genRandomFreeSpin = randomBoolean();
                GlobalSettings bonusPercentage = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.BONUS_SETTINGS_NAME);
                isBonus = genRandomFreeSpin <= Double.parseDouble(bonusPercentage.getValue());
            }else {
                isBonus = false;
            }

            boolean isFreeSpin;
            if(Bet.hasFreeSpin() && !isBonus){
                double genRandomFreeSpin = randomBoolean();
                GlobalSettings freeSpinPercentage = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.FREE_SPIN_SETTINGS_NAME);
                isFreeSpin = genRandomFreeSpin <= Double.parseDouble(freeSpinPercentage.getValue());
            }else {
                isFreeSpin = false;
            }

            GlobalSettings winPerc = (GlobalSettings) em.get(GlobalSettings.class,StaticDefinitions.WIN_PERCENTAGE_SETTINGS_NAME);
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
            if(isBonus){
                response.put("maxBonus",bonusCash.getCash() * Double.parseDouble(bonusGiveAwaySettings.getValue()) / 100);
            }
            response.put("_win_",isWin);
            if(isWin) {
                response.put("_maxWin_", gameCash.getCash());
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