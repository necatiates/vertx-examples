package com.cas.web.app.handlers;

import com.cas.cache.CacheManager;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.PokerBet;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.persistence.EntityManager;
import java.util.Random;

/**
 * Created by tolga on 26.03.2016.
 */
public class PokerBetHandler {
    public static void bet(RoutingContext routingContext){
        final PokerBet pokerBet = Json.decodeValue(routingContext.getBodyAsString(),PokerBet.class);

        EntityManager entityManager = Server.factory.createEntityManager();
        entityManager.getTransaction().begin();
        Cash cash = entityManager.find(Cash.class,"Cards");
        User user = entityManager.find(User.class,((User)routingContext.session().get("user")).getUsername());
        pokerBet.setUsername(user.getUsername());


        JsonObject response = BetDesicionHelper.invoke(pokerBet, cash);
        response.put("freeSpin",false);
        response.put("bonus",false);
        entityManager.persist(pokerBet);

        cash.setCash(cash.getCash() + pokerBet.getBet());
        user.setCash(user.getCash() - pokerBet.getBet());
        entityManager.getTransaction().commit();
        response.put("id",pokerBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;
    }



}
