package com.cas.web.app.handlers.game.accept;

import com.cas.service.model.SlotBetResult;
import com.cas.service.model.StrachResult;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.SlotBet;
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
public class StrachBetAcceptHandler {
    public static void accept(RoutingContext routingContext){
        final StrachResult strachResult = Json.decodeValue(routingContext.getBodyAsString(),StrachResult.class);
        Session entityManager = Server.factory.openSession();
        User user = (User) entityManager.get(User.class,
                ((User)routingContext.session().get("user")).getUsername());

        entityManager.getTransaction().begin();

        user.setCash(user.getCash() + strachResult.getTotalWin());
        entityManager.persist(user);

        Cash cash = (Cash) entityManager.get(Cash.class,"Strach");
        cash.setCash(cash.getCash() - strachResult.getTotalWin());
        entityManager.persist(cash);

        StrachBet storedStrach = (StrachBet) entityManager.get(StrachBet.class,strachResult.getId());
        storedStrach.setTotalWin(strachResult.getTotalWin());
        storedStrach.setUpdate_time(System.currentTimeMillis());

        entityManager.merge(storedStrach);
        entityManager.getTransaction().commit();
        entityManager.close();

        JsonObject response = new JsonObject();
        response.put("_accepted_",true);
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        return;
    }
}
