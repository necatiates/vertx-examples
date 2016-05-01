package com.cas.web.app.handlers.game.desicion;

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

        Session entityManager = Server.factory.openSession();
        entityManager.getTransaction().begin();
        Cash cash = (Cash) entityManager.get(Cash.class,"Cards");
        User user = (User) entityManager.get(User.class,((User)routingContext.session().get("user")).getUsername());
        blackJackBet.setUsername(user.getUsername());


        JsonObject response = BetDesicionHelper.invoke(blackJackBet,entityManager);
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
        Session entityManager = Server.factory.openSession();
        Criteria criteria = entityManager.createCriteria(BlackJackBet.class);
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