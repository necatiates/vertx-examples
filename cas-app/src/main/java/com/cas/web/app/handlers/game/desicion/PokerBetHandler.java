package com.cas.web.app.handlers.game.desicion;

import com.cas.StaticDefinitions;
import com.cas.service.model.PokerHistoryRequest;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.PokerBet;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import com.cas.web.app.handlers.game.desicion.BetDesicionHelper;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by tolga on 26.03.2016.
 */
public class PokerBetHandler {
    public static void bet(RoutingContext routingContext){
        final PokerBet pokerBet = Json.decodeValue(routingContext.getBodyAsString(),PokerBet.class);

        Session entityManager = Server.factory.openSession();
        entityManager.getTransaction().begin();
        Cash cash = (Cash) entityManager.get(Cash.class, StaticDefinitions.GAME_CASH_NAME);
        User user = (User) entityManager.get(User.class,((User)routingContext.session().get(StaticDefinitions.USER_SESSION_KEY)).getUsername());
        pokerBet.setUsername(user.getUsername());


        JsonObject response = BetDesicionHelper.invoke(pokerBet,entityManager);
        response.put("freeSpin",false);
        response.put("bonus",false);
        entityManager.persist(pokerBet);

        cash.setCash(cash.getCash() + pokerBet.getBet());
        user.setCash(user.getCash() - pokerBet.getBet());
        entityManager.getTransaction().commit();
        response.put("id",pokerBet.getId());

        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        entityManager.close();
        return;
    }
    public static void getGames(RoutingContext routingContext) {
        final PokerHistoryRequest pokerHistoryRequest =  Json.decodeValue(routingContext.getBodyAsString(),PokerHistoryRequest.class);
        Session entityManager = Server.factory.openSession();
        Criteria criteria = entityManager.createCriteria(PokerBet.class);
        criteria.setFirstResult(0 + (pokerHistoryRequest.getPage() - 1) * 25);
        criteria.setMaxResults(25 + (pokerHistoryRequest.getPage() - 1) * 25);
        if(pokerHistoryRequest.getUsername() != null && !pokerHistoryRequest.getUsername().equals("")) {
            criteria.add(Restrictions.eq("username",pokerHistoryRequest.getUsername()));
        }
        List<PokerBet> result = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(result));
        entityManager.close();
    }
}
