package com.cas.web.app.handlers.game.accept;

import com.cas.StaticDefinitions;
import com.cas.spring.entity.Cash;
import com.cas.service.model.SlotBetResult;
import com.cas.spring.entity.SlotBet;
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
public class SlotMachineBetAcceptHandler {
    public static void accept(RoutingContext routingContext){
        final SlotBetResult slotBet = Json.decodeValue(routingContext.getBodyAsString(),SlotBetResult.class);
        Session entityManager = Server.factory.openSession();
        User user = (User) entityManager.get(User.class,
                ((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());


        JsonObject response = new JsonObject();
        SlotBet storedSlotBet = (SlotBet) entityManager.get(SlotBet.class,slotBet.getId());

        if(slotBet.getTotalWin() <= storedSlotBet.getMaxWin()) {
            entityManager.getTransaction().begin();

            user.setCash(user.getCash() + slotBet.getTotalWin());

            entityManager.persist(user);

            Cash cash = (Cash) entityManager.get(Cash.class, StaticDefinitions.GAME_CASH_NAME);
            cash.setCash(cash.getCash() - slotBet.getTotalWin());
            entityManager.persist(cash);


            storedSlotBet.setTotalWin(slotBet.getTotalWin());
            storedSlotBet.setNumLineWin(slotBet.getNumLineWin());
            storedSlotBet.setUpdate_time(System.currentTimeMillis());
            storedSlotBet.setUserBalanceAfterPlay(user.getCash());
            storedSlotBet.setCashBalanceAfterPlay(cash.getCash());

            entityManager.merge(storedSlotBet);
            entityManager.getTransaction().commit();

            response.put("_accepted_", true);
        }else{
            response.put("_accepted_", false);
        }
        entityManager.close();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        return;
    }
}
