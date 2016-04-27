package com.cas.web.app.handlers.game.desicion;

import com.cas.service.model.SlotGamesHistoryRequest;
import com.cas.spring.entity.*;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by tolga on 06.03.2016.
 */
public class SlotMachineBetHandler {
    public static void bet(RoutingContext routingContext){
        final SlotBet slotBet = Json.decodeValue(routingContext.getBodyAsString(),SlotBet.class);

        Session entityManager = Server.factory.openSession();
        entityManager.getTransaction().begin();
        Cash cash = (Cash) entityManager.get(Cash.class,"Slots");
        User user = (User) entityManager.get(User.class,((User)routingContext.session().get("user")).getUsername());
        slotBet.setUsername(user.getUsername());


        JsonObject response = BetDesicionHelper.invoke(slotBet, cash);
        entityManager.persist(slotBet);

        cash.setCash(cash.getCash() + slotBet.getBet() * slotBet.getLineCount());
        user.setCash(user.getCash() - slotBet.getBet() * slotBet.getLineCount());
        entityManager.getTransaction().commit();
        response.put("id",slotBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;
    }

    public static void getGames(RoutingContext routingContext) {
        final SlotGamesHistoryRequest slotGamesHistoryRequest =  Json.decodeValue(routingContext.getBodyAsString(),SlotGamesHistoryRequest.class);
        Session entityManager = Server.factory.openSession();
        Criteria criteria = entityManager.createCriteria(SlotBet.class);
        criteria.setFirstResult(0 + (slotGamesHistoryRequest.getPage() - 1) * 25);
        criteria.setMaxResults(25 + (slotGamesHistoryRequest.getPage() - 1) * 25);
        if(slotGamesHistoryRequest.getUsername() != null && !slotGamesHistoryRequest.getUsername().equals("")) {
            criteria.add(Restrictions.eq("username",slotGamesHistoryRequest.getUsername()));
        }
        List<TransferCheckout> result = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(result));
        entityManager.close();
    }
    public static boolean randomBoolean(){
        return Math.random() < 0.5;
    }
}
