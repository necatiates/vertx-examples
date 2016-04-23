package com.cas.web.app.handlers;

import com.cas.cache.CacheManager;
import com.cas.service.model.SlotGamesHistoryRequest;
import com.cas.spring.entity.*;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Random;

/**
 * Created by tolga on 06.03.2016.
 */
public class SlotMachineBetHandler {
    public static void bet(RoutingContext routingContext){
        final SlotBet slotBet = Json.decodeValue(routingContext.getBodyAsString(),SlotBet.class);

        JsonObject response = new JsonObject();

        EntityManager entityManager = Server.factory.createEntityManager();
        entityManager.getTransaction().begin();
        Cash cash = entityManager.find(Cash.class,"Slots");
        User user = entityManager.find(User.class,((User)routingContext.session().get("user")).getUsername());
        slotBet.setUsername(user.getUsername());


        double ratio = 1;
        /*
            Decide to win or not
        */
        Double avaliableCash = ((cash.getCapital() * 0.20 + cash.getCash())  / 2 ) / 5;
        if(slotBet.getMinWin() < avaliableCash && ratio < 1){
            slotBet.setWinResult(true);
            response.put("_win_",true);
            response.put("_maxWin_",avaliableCash);
            slotBet.setLoseCause("WinRatio");
        }else if(slotBet.getMinWin() < avaliableCash && ratio == 1){
            boolean genRandom = randomBoolean();
            slotBet.setWinResult(genRandom);
            if(genRandom){
                slotBet.setLoseCause("WinRandom");
            }else{
                slotBet.setLoseCause("LoseRandom");
            }
            response.put("_win_",genRandom);
            response.put("_maxWin_",avaliableCash);
        } else{
            slotBet.setWinResult(false);
            response.put("_win_",false);
            response.put("_maxWin_",0);
            slotBet.setLoseCause("LoseRatio");
        }
        entityManager.persist(slotBet);

        cash.setCash(cash.getCash() + slotBet.getBet() * slotBet.getLineCount());
        user.setCash(user.getCash() - slotBet.getBet() * slotBet.getLineCount());
        entityManager.getTransaction().commit();
        response.put("id",slotBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;
    }

    public static void getGames(RoutingContext routingContext) {
        final SlotGamesHistoryRequest slotGamesHistoryRequest =  Json.decodeValue(routingContext.getBodyAsString(),SlotGamesHistoryRequest.class);
        EntityManager entityManager = Server.factory.createEntityManager();
        Criteria criteria = ((Session)entityManager.getDelegate()).createCriteria(SlotBet.class);
        criteria.setFirstResult(0 + (slotGamesHistoryRequest.getPage() - 1) * 25);
        criteria.setMaxResults(25 + (slotGamesHistoryRequest.getPage() - 1) * 25);
        if(slotGamesHistoryRequest.getUsername() != null && !slotGamesHistoryRequest.getUsername().equals("")) {
            criteria.add(Restrictions.eq("username",slotGamesHistoryRequest.getUsername()));
        }
        List<TransferCheckout> result = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(result));
        entityManager.close();
    }
    public static boolean randomBoolean(){
        return Math.random() < 0.5;
    }
}
