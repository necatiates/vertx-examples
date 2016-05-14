package com.cas.web.app.handlers.game.accept;

import com.cas.StaticDefinitions;
import com.cas.service.model.BingoResult;
import com.cas.service.model.BlackJackResult;
import com.cas.spring.entity.BingoBet;
import com.cas.spring.entity.BlackJackBet;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;

/**
 * Created by Administrator on 12.05.2016.
 */
public class BingoBetAcceptHandler {
    public static void accept(RoutingContext routingContext){
        final BingoResult bingoResult = Json.decodeValue(routingContext.getBodyAsString(),BingoResult.class);
        Session entityManager = Server.factory.openSession();
        User user = (User) entityManager.get(User.class,
                ((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());

        entityManager.getTransaction().begin();
        user.setCash(user.getCash() + bingoResult.getTotalWin());
        entityManager.persist(user);

        Cash cash = (Cash) entityManager.get(Cash.class,StaticDefinitions.GAME_CASH_NAME);
        cash.setCash(cash.getCash() - bingoResult.getTotalWin());
        entityManager.persist(cash);

        BingoBet bingoBet = (BingoBet) entityManager.get(BingoBet.class,bingoResult.getId());
        bingoBet.setTotalWin(bingoResult.getTotalWin());
        bingoBet.setUpdate_time(System.currentTimeMillis());
        JsonObject response = new JsonObject();
        response.put("_accepted_",true);
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        return;
    }
}
