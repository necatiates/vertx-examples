package com.cas.web.app.handlers.game.desicion;

import com.cas.StaticDefinitions;
import com.cas.service.model.SlotGamesHistoryRequest;
import com.cas.spring.entity.*;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * Created by tolga on 06.03.2016.
 */
public class SlotMachineBetHandler {
    public static void bet(RoutingContext routingContext){
        final SlotBet slotBet = Json.decodeValue(routingContext.getBodyAsString(),SlotBet.class);

        Session entityManager = Server.factory.openSession();
        entityManager.getTransaction().begin();
        Cash cash = (Cash) entityManager.get(Cash.class, StaticDefinitions.GAME_CASH_NAME);
        User user = (User) entityManager.get(User.class,((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());
        slotBet.setUsername(user.getUsername());
        slotBet.setUpdate_time(new Date().getTime());


        JsonObject response = BetDesicionHelper.invoke(slotBet,entityManager);
        entityManager.persist(slotBet);

        if(slotBet.getCurFreeSpinCnt() == 0) {
            cash.setCash(cash.getCash() + slotBet.getBet() * slotBet.getLineCount());
            user.setCash(user.getCash() - slotBet.getBet() * slotBet.getLineCount());
        }
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
        criteria.addOrder(Order.desc("update_time"));
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
