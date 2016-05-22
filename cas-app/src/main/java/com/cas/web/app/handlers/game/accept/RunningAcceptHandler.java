package com.cas.web.app.handlers.game.accept;

import com.cas.StaticDefinitions;
import com.cas.service.model.RunningResult;
import com.cas.service.model.StrachResult;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.RunningBet;
import com.cas.spring.entity.StrachBet;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;

/**
 * Created by tolga on 06.03.2016.
 */
public class RunningAcceptHandler {
    public static void accept(RoutingContext routingContext){
        final RunningResult runningResult = Json.decodeValue(routingContext.getBodyAsString(),RunningResult.class);
        Session entityManager = Server.factory.openSession();
        User user = (User) entityManager.get(User.class,
                ((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());

        entityManager.getTransaction().begin();

        user.setCash(user.getCash() + runningResult.getTotalWin());
        entityManager.persist(user);

        Cash cash = (Cash) entityManager.get(Cash.class,StaticDefinitions.GAME_CASH_NAME);
        cash.setCash(cash.getCash() - runningResult.getTotalWin());
        entityManager.persist(cash);

        RunningBet storedRunningBet = (RunningBet) entityManager.get(RunningBet.class,runningResult.getId());
        storedRunningBet.setTotalWin(runningResult.getTotalWin());
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
