package com.cas.web.app.handlers.game.accept;

import com.cas.StaticDefinitions;
import com.cas.service.model.MinerResult;
import com.cas.service.model.RunningResult;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.MinerBet;
import com.cas.spring.entity.RunningBet;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;

/**
 * Created by tolga on 06.03.2016.
 */
public class MinerAcceptHandler {
    public static void accept(RoutingContext routingContext){
        final MinerResult minerResult = Json.decodeValue(routingContext.getBodyAsString(),MinerResult.class);
        Session entityManager = Server.factory.openSession();
        User user = (User) entityManager.get(User.class,
                ((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());

        entityManager.getTransaction().begin();

        user.setCash(user.getCash() + minerResult.getTotalWin());
        entityManager.persist(user);

        Cash cash = (Cash) entityManager.get(Cash.class,StaticDefinitions.GAME_CASH_NAME);
        cash.setCash(cash.getCash() - minerResult.getTotalWin());
        entityManager.persist(cash);

        MinerBet storedRunningBet = (MinerBet) entityManager.get(MinerBet.class,minerResult.getId());
        storedRunningBet.setTotalWin(minerResult.getTotalWin());
        storedRunningBet.setUpdate_time(System.currentTimeMillis());

        storedRunningBet.setCashBalanceAfterPlay(cash.getCash());
        storedRunningBet.setUserBalanceAfterPlay(user.getCash());

        entityManager.merge(storedRunningBet);
        entityManager.getTransaction().commit();
        entityManager.close();

        JsonObject response = new JsonObject();
        response.put("_accepted_",true);
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        return;
    }
}
