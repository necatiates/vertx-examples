package com.cas.web.app.handlers.game.desicion;

import com.cas.StaticDefinitions;
import com.cas.spring.entity.BingoBet;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;

import java.util.Date;

/**
 * Created by Administrator on 12.05.2016.
 */
public class BingoBetHandler {
    public static void bet(RoutingContext routingContext){
        final BingoBet bingoBet = Json.decodeValue(routingContext.getBodyAsString(),BingoBet.class);
        Session entityManager = Server.factory.openSession();

        entityManager.getTransaction().begin();
        Cash cash = (Cash) entityManager.get(Cash.class, StaticDefinitions.GAME_CASH_NAME);
        User user = (User) entityManager.get(User.class,((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());
        bingoBet.setUsername(user.getUsername());
        bingoBet.setUpdate_time(System.currentTimeMillis());
        JsonObject response = BetDesicionHelper.invoke(bingoBet,entityManager);
        entityManager.persist(bingoBet);
        cash.setCash(cash.getCash() + bingoBet.getBet());
        user.setCash(user.getCash() - bingoBet.getBet());
        entityManager.getTransaction().commit();

        response.put("id",bingoBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;

    }
}
