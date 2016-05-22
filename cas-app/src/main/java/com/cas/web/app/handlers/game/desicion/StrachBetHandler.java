package com.cas.web.app.handlers.game.desicion;

import com.cas.StaticDefinitions;
import com.cas.service.model.StrachHistoryRequest;
import com.cas.spring.entity.*;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Random;

/**
 * Created by tolga on 06.03.2016.
 */
public class StrachBetHandler {
    public static void bet(RoutingContext routingContext){
        final StrachBet strachBet = Json.decodeValue(routingContext.getBodyAsString(),StrachBet.class);

        Session entityManager = Server.factory.openSession();
        entityManager.getTransaction().begin();
        Cash cash = (Cash) entityManager.get(Cash.class, StaticDefinitions.GAME_CASH_NAME);
        User user = (User) entityManager.get(User.class,((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());
        strachBet.setUsername(user.getUsername());
        strachBet.setUpdate_time(System.currentTimeMillis());

        JsonObject response = BetDesicionHelper.invoke(strachBet,entityManager);
        entityManager.persist(strachBet);

        cash.setCash(cash.getCash() + strachBet.getBet());
        user.setCash(user.getCash() - strachBet.getBet());

        strachBet.setCashBalanceAfterPlay(cash.getCash());
        strachBet.setUserBalanceAfterPlay(user.getCash());

        entityManager.getTransaction().commit();
        response.put("id",strachBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;
    }
    public static void getGames(RoutingContext routingContext) {
        final StrachHistoryRequest strachHistoryRequest =  Json.decodeValue(routingContext.getBodyAsString(),StrachHistoryRequest.class);
        Session entityManager = Server.factory.openSession();
        Criteria criteria = entityManager.createCriteria(StrachBet.class);
        criteria.setFirstResult(0 + (strachHistoryRequest.getPage() - 1) * 25);
        criteria.setMaxResults(25 + (strachHistoryRequest.getPage() - 1) * 25);
        criteria.addOrder(Order.desc("update_time"));

        if(strachHistoryRequest.getUsername() != null && !strachHistoryRequest.getUsername().equals("")) {
            criteria.add(Restrictions.eq("username",strachHistoryRequest.getUsername()));
        }
        List<TransferCheckout> result = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(result));
        entityManager.close();
    }
}
