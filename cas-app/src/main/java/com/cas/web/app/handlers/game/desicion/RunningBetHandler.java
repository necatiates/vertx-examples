package com.cas.web.app.handlers.game.desicion;

import com.cas.StaticDefinitions;
import com.cas.service.model.RunningHistoryRequest;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.RunningBet;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by tolga on 06.03.2016.
 */
public class RunningBetHandler {
    public static void bet(RoutingContext routingContext){
        final RunningBet runBet = Json.decodeValue(routingContext.getBodyAsString(),RunningBet.class);

        Session entityManager = Server.factory.openSession();
        entityManager.getTransaction().begin();
        Cash cash = (Cash) entityManager.get(Cash.class, StaticDefinitions.GAME_CASH_NAME);
        User user = (User) entityManager.get(User.class,((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());
        runBet.setUsername(user.getUsername());
        runBet.setUpdate_time(System.currentTimeMillis());

        JsonObject response = BetDesicionHelper.invoke(runBet,entityManager, 1);
        entityManager.persist(runBet);

        cash.setCash(cash.getCash() + runBet.getBet());
        user.setCash(user.getCash() - runBet.getBet());

        runBet.setCashBalanceAfterPlay(cash.getCash());
        runBet.setUserBalanceAfterPlay(user.getCash());

        entityManager.getTransaction().commit();
        response.put("id",runBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;
    }
    public static void getGames(RoutingContext routingContext) {
        final RunningHistoryRequest strachHistoryRequest =  Json.decodeValue(routingContext.getBodyAsString(),RunningHistoryRequest.class);
        Session entityManager = Server.factory.openSession();
        Criteria criteria = entityManager.createCriteria(RunningBet.class);
        criteria.setFirstResult(0 + (strachHistoryRequest.getPage() - 1) * 25);
        criteria.setMaxResults(25 + (strachHistoryRequest.getPage() - 1) * 25);
        criteria.addOrder(Order.desc("update_time"));

        if(strachHistoryRequest.getUsername() != null && !strachHistoryRequest.getUsername().equals("")) {
            criteria.add(Restrictions.eq("username",strachHistoryRequest.getUsername()));
        }
        List<RunningBet> result = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(result));
        entityManager.close();
    }
}
