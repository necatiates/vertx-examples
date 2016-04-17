package com.cas.web.app.handlers;

import com.cas.cache.CacheManager;
import com.cas.spring.entity.BlackJackBet;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.StrachBet;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.persistence.EntityManager;
import java.util.Random;

/**
 * Created by tolga on 06.03.2016.
 */
public class BlackJackBetHandler {
    private static Random random = new Random();
    public static void bet(RoutingContext routingContext){
        final BlackJackBet strachBet = Json.decodeValue(routingContext.getBodyAsString(),BlackJackBet.class);

        JsonObject response = new JsonObject();

        EntityManager entityManager = Server.factory.createEntityManager();
        entityManager.getTransaction().begin();
        Cash cash = entityManager.find(Cash.class,"Cards");
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
        }else if(strachBet.getMinWin() < avaliableCash && ratio == 1){
            boolean genRandom = random.nextBoolean();
            strachBet.setWinResult(genRandom);
            response.put("_win_",genRandom);
            response.put("_maxWin_",avaliableCash);
        } else{
            strachBet.setWinResult(false);
            response.put("_win_",false);
            response.put("_maxWin_",0);
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
}
