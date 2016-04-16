package com.cas.web.app.handlers;

import com.cas.cache.CacheManager;
import com.cas.spring.entity.Cash;
import com.cas.service.model.SlotBetResult;
import com.cas.spring.entity.SlotBet;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import javax.persistence.EntityManager;

/**
 * Created by tolga on 06.03.2016.
 */
public class SlotMachineBetAcceptHandler {
    public static void accept(RoutingContext routingContext){
        final SlotBetResult slotBet = Json.decodeValue(routingContext.getBodyAsString(),SlotBetResult.class);
        EntityManager entityManager = Server.factory.createEntityManager();
        User user = entityManager.find(User.class,
                ((User)routingContext.session().get("user")).getUsername());

        entityManager.getTransaction().begin();

        user.setCash(user.getCash() + slotBet.getTotalWin());
        CacheManager.getHapinessCache()
                .get(routingContext.session().id())
                .addGameResult(slotBet.getTotalWin());

        entityManager.persist(user);

        Cash cash = entityManager.find(Cash.class,"Slots");
        cash.setCash(cash.getCash() - slotBet.getTotalWin());
        entityManager.persist(cash);

        SlotBet storedSlotBet = entityManager.find(SlotBet.class,slotBet.getId());
        storedSlotBet.setTotalWin(slotBet.getTotalWin());
        storedSlotBet.setNumLineWin(slotBet.getNumLineWin());
        storedSlotBet.setUpdate_time(System.currentTimeMillis());

        entityManager.merge(storedSlotBet);
        entityManager.getTransaction().commit();
        entityManager.close();

        JsonObject response = new JsonObject();
        response.put("_accepted_",true);
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        return;
    }
}
