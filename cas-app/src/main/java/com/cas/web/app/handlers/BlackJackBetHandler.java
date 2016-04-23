package com.cas.web.app.handlers;

import com.cas.cache.CacheManager;
import com.cas.service.model.BlackJackHistoryRequest;
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
import java.util.List;
import java.util.Random;

/**
 * Created by tolga on 06.03.2016.
 */
public class BlackJackBetHandler {
    private static Random random = new Random();
    public static void bet(RoutingContext routingContext){
        final BlackJackBet blackJackBet = Json.decodeValue(routingContext.getBodyAsString(),BlackJackBet.class);

        JsonObject response = new JsonObject();

        EntityManager entityManager = Server.factory.createEntityManager();
        entityManager.getTransaction().begin();
        Cash cash = entityManager.find(Cash.class,"Cards");
        User user = entityManager.find(User.class,((User)routingContext.session().get("user")).getUsername());
        blackJackBet.setUsername(user.getUsername());


        double ratio = CacheManager.getHappinessRatio();
        /*
            Decide to win or not
        */
        Double avaliableCash = ((cash.getCapital() * 0.20 + cash.getCash())  / 2 ) / 5;
        if(blackJackBet.getMinWin() < avaliableCash && ratio < 1){
            blackJackBet.setWinResult(true);
            response.put("_win_",true);
            response.put("_maxWin_",avaliableCash);
            blackJackBet.setLoseCause("WinRatio");
        }else if(blackJackBet.getMinWin() < avaliableCash && ratio == 1){
            boolean genRandom = random.nextBoolean();
            blackJackBet.setWinResult(genRandom);
            if(genRandom){
                blackJackBet.setLoseCause("WinRandom");
            }else {
                blackJackBet.setLoseCause("LoseRandom");
            }
            response.put("_win_",genRandom);
            response.put("_maxWin_",avaliableCash);
        } else{
            blackJackBet.setWinResult(false);
            response.put("_win_",false);
            response.put("_maxWin_",0);
            blackJackBet.setLoseCause("LoseRatio");
        }
        entityManager.persist(blackJackBet);

        cash.setCash(cash.getCash() + blackJackBet.getBet());
        user.setCash(user.getCash() - blackJackBet.getBet());
        entityManager.getTransaction().commit();
        response.put("id",blackJackBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;
    }
    public static void getGames(RoutingContext routingContext) {
        final BlackJackHistoryRequest blackJackHistoryRequest =  Json.decodeValue(routingContext.getBodyAsString(),BlackJackHistoryRequest.class);
        EntityManager entityManager = Server.factory.createEntityManager();
        Criteria criteria = ((Session)entityManager.getDelegate()).createCriteria(BlackJackBet.class);
        criteria.setFirstResult(0 + (blackJackHistoryRequest.getPage() - 1) * 25);
        criteria.setMaxResults(25 + (blackJackHistoryRequest.getPage() - 1) * 25);
        if(blackJackHistoryRequest.getUsername() != null && !blackJackHistoryRequest.getUsername().equals("")) {
            criteria.add(Restrictions.eq("username",blackJackHistoryRequest.getUsername()));
        }
        List<BlackJackBet> result = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(result));
        entityManager.close();
    }
}
