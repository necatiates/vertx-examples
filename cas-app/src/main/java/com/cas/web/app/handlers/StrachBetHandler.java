package com.cas.web.app.handlers;

import com.cas.cache.CacheManager;
import com.cas.service.model.SlotGamesHistoryRequest;
import com.cas.service.model.StrachHistoryRequest;
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
public class StrachBetHandler {
    private static Random random = new Random();
    public static void bet(RoutingContext routingContext){
        final StrachBet strachBet = Json.decodeValue(routingContext.getBodyAsString(),StrachBet.class);

        JsonObject response = new JsonObject();

        EntityManager entityManager = Server.factory.createEntityManager();
        entityManager.getTransaction().begin();
        Cash cash = entityManager.find(Cash.class,"Strach");
        User user = entityManager.find(User.class,((User)routingContext.session().get("user")).getUsername());
        strachBet.setUsername(user.getUsername());


        double ratio = CacheManager.getHappinessRatio();
        /*
            Decide to win or not
        */
        Double avaliableCash = ((cash.getCapital() * 0.20 + cash.getCash())  / 2 ) / 5;
        if(strachBet.getMinWin() < avaliableCash && ratio < 1){
            strachBet.setWinResult(true);
            response.put("_win_",true);
            response.put("_maxWin_",avaliableCash);
            strachBet.setLoseCause("WinRatio");
        }else if(strachBet.getMinWin() < avaliableCash && ratio == 1){
            boolean genRandom = random.nextBoolean();
            strachBet.setWinResult(genRandom);
            if(genRandom){
                strachBet.setLoseCause("WinRandom");
            }else{
                strachBet.setLoseCause("LoseRandom");
            }
            response.put("_win_",genRandom);
            response.put("_maxWin_",avaliableCash);
        } else{
            strachBet.setWinResult(false);
            response.put("_win_",false);
            response.put("_maxWin_",0);
            strachBet.setLoseCause("LoseRatio");
        }
        entityManager.persist(strachBet);

        cash.setCash(cash.getCash() + strachBet.getBet());
        user.setCash(user.getCash() - strachBet.getBet());
        entityManager.getTransaction().commit();
        response.put("id",strachBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;
    }
    public static void getGames(RoutingContext routingContext) {
        final StrachHistoryRequest strachHistoryRequest =  Json.decodeValue(routingContext.getBodyAsString(),StrachHistoryRequest.class);
        EntityManager entityManager = Server.factory.createEntityManager();
        Criteria criteria = ((Session)entityManager.getDelegate()).createCriteria(StrachBet.class);
        criteria.setFirstResult(0 + (strachHistoryRequest.getPage() - 1) * 25);
        criteria.setMaxResults(25 + (strachHistoryRequest.getPage() - 1) * 25);
        if(strachHistoryRequest.getUsername() != null && !strachHistoryRequest.getUsername().equals("")) {
            criteria.add(Restrictions.eq("username",strachHistoryRequest.getUsername()));
        }
        List<TransferCheckout> result = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(result));
        entityManager.close();
    }
}
