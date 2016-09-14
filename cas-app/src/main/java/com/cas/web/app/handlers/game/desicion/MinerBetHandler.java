package com.cas.web.app.handlers.game.desicion;

import com.cas.StaticDefinitions;
import com.cas.service.model.MinerHistoryRequest;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.MinerBet;
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
public class MinerBetHandler {
    public static void bet(RoutingContext routingContext){
        final MinerBet minerBet = Json.decodeValue(routingContext.getBodyAsString(),MinerBet.class);

        Session entityManager = Server.factory.openSession();
        entityManager.getTransaction().begin();
        Cash cash = (Cash) entityManager.get(Cash.class, StaticDefinitions.GAME_CASH_NAME);
        User user = (User) entityManager.get(User.class,((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());
        minerBet.setUsername(user.getUsername());
        minerBet.setUpdate_time(System.currentTimeMillis());

        JsonObject response = BetDesicionHelper.invoke(minerBet,entityManager, 1);
        entityManager.persist(minerBet);

        cash.setCash(cash.getCash() + minerBet.getBet());
        user.setCash(user.getCash() - minerBet.getBet());

        minerBet.setCashBalanceAfterPlay(cash.getCash());
        minerBet.setUserBalanceAfterPlay(user.getCash());

        entityManager.getTransaction().commit();
        response.put("id",minerBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;
    }
    public static void getGames(RoutingContext routingContext) {
        final MinerHistoryRequest minerHistoryRequest =  Json.decodeValue(routingContext.getBodyAsString(),MinerHistoryRequest.class);
        Session entityManager = Server.factory.openSession();
        Criteria criteria = entityManager.createCriteria(RunningBet.class);
        criteria.setFirstResult(0 + (minerHistoryRequest.getPage() - 1) * 25);
        criteria.setMaxResults(25 + (minerHistoryRequest.getPage() - 1) * 25);
        criteria.addOrder(Order.desc("update_time"));

        if(minerHistoryRequest.getUsername() != null && !minerHistoryRequest.getUsername().equals("")) {
            criteria.add(Restrictions.eq("username",minerHistoryRequest.getUsername()));
        }
        List<RunningBet> result = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(result));
        entityManager.close();
    }
}
