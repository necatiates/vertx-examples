package com.cas.web.app.handlers;

import com.cas.cache.CacheManager;
import com.cas.service.model.PokerResult;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.PokerBet;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.persistence.EntityManager;

/**
 * Created by tolga on 26.03.2016.
 */
public class PokerBetAcceptHandler {
    public static void accept(RoutingContext routingContext){
        final PokerResult pokerBetResult = Json.decodeValue(routingContext.getBodyAsString(),PokerResult.class);
        EntityManager entityManager = Server.factory.createEntityManager();
        User user = entityManager.find(User.class,
                ((User)routingContext.session().get("user")).getUsername());
        entityManager.getTransaction().begin();
        user.setCash(user.getCash() + pokerBetResult.getTotalWin());
        CacheManager.getHapinessCache()
                .get(routingContext.session().id())
                .addGameResult(pokerBetResult.getTotalWin());
        entityManager.persist(user);

        Cash cash = entityManager.find(Cash.class,"Cards");
        cash.setCash(cash.getCash() - pokerBetResult.getTotalWin());
        entityManager.persist(cash);

        PokerBet storedPokerBet = entityManager.find(PokerBet.class,pokerBetResult.getId());
        storedPokerBet.setTotalWin(pokerBetResult.getTotalWin());
        storedPokerBet.setUpdate_time(System.currentTimeMillis());

        entityManager.merge(storedPokerBet);
        entityManager.getTransaction().commit();
        entityManager.close();
        JsonObject response = new JsonObject();
        response.put("_accepted_",true);
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        return;
    }
}
