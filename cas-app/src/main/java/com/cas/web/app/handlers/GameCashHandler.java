package com.cas.web.app.handlers;

import com.cas.spring.entity.Cash;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by tolga on 15.03.2016.
 */
public class GameCashHandler {
    public static void getCashes(RoutingContext routingContext){
        EntityManager em = Server.factory.createEntityManager();
        CriteriaQuery<Cash> criteria = em.getCriteriaBuilder().createQuery(Cash.class);
        criteria.select(criteria.from(Cash.class));
        List<Cash> cashList = em.createQuery(criteria).getResultList();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(cashList));
        em.close();
    }

    public static void updateCash(RoutingContext routingContext) {
        EntityManager em = Server.factory.createEntityManager();
        Cash cash = Json.decodeValue(routingContext.getBodyAsString(),Cash.class);
        em.getTransaction().begin();
        em.merge(cash);
        em.getTransaction().commit();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(cash));
        em.close();

    }
}
