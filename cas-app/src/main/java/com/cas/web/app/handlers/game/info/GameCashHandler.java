package com.cas.web.app.handlers.game.info;

import com.cas.spring.entity.Cash;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Criteria;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by tolga on 15.03.2016.
 */
public class GameCashHandler {
    public static void getCashes(RoutingContext routingContext){
        Session em = Server.factory.openSession();
        Criteria criteria = em.createCriteria(Cash.class);
        List<Cash> cashList = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(cashList));
        em.close();
    }

    public static void updateCash(RoutingContext routingContext) {
        Session em = Server.factory.openSession();
        Cash cash = Json.decodeValue(routingContext.getBodyAsString(),Cash.class);
        em.getTransaction().begin();
        em.merge(cash);
        em.getTransaction().commit();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(cash));
        em.close();

    }
}
