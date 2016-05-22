package com.cas.web.app.handlers.game.accept;

import com.cas.StaticDefinitions;
import com.cas.service.model.BlackJackResult;
import com.cas.spring.entity.BlackJackBet;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.StrachBet;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;

import javax.persistence.EntityManager;

/**
 * Created by tolga on 06.03.2016.
 */
public class BlackJackBetAcceptHandler {
    public static void accept(RoutingContext routingContext){
        final BlackJackResult strachResult = Json.decodeValue(routingContext.getBodyAsString(),BlackJackResult.class);
        Session entityManager = Server.factory.openSession();
        User user = (User) entityManager.get(User.class,
                ((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());

        entityManager.getTransaction().begin();

        user.setCash(user.getCash() + strachResult.getTotalWin());


        entityManager.persist(user);

        Cash cash = (Cash) entityManager.get(Cash.class,StaticDefinitions.GAME_CASH_NAME);
        cash.setCash(cash.getCash() - strachResult.getTotalWin());
        entityManager.persist(cash);

        BlackJackBet blackJackBet = (BlackJackBet) entityManager.get(BlackJackBet.class,strachResult.getId());
        blackJackBet.setTotalWin(strachResult.getTotalWin());
        blackJackBet.setUpdate_time(System.currentTimeMillis());
        blackJackBet.setCashBalanceAfterPlay(cash.getCash());
        blackJackBet.setUserBalanceAfterPlay(user.getCash());

        entityManager.merge(blackJackBet);
        entityManager.getTransaction().commit();
        entityManager.close();

        JsonObject response = new JsonObject();
        response.put("_accepted_",true);
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        return;
    }
}
