package com.cas.web.app.handlers;

import com.cas.service.model.CheckoutsRequest;
import com.cas.spring.entity.TransferCheckout;
import com.cas.spring.entity.User;
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
 * Created by tolga on 09.04.2016.
 */
public class TransferCheckoutHandler {
    public static void handle(RoutingContext routingContext){
        final TransferCheckout transferRequest = Json.decodeValue(routingContext.getBodyAsString(),TransferCheckout.class);
        transferRequest.setUsername(((User)routingContext.session().get("user")).getUsername());
        EntityManager em = Server.factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(transferRequest);
        em.getTransaction().commit();
        em.close();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(new JsonObject().put("id",transferRequest.getId())));
    }
    public static void getTransfers(RoutingContext routingContext){
        final CheckoutsRequest transferRequest = Json.decodeValue(routingContext.getBodyAsString(),CheckoutsRequest.class);
        EntityManager em = Server.factory.createEntityManager();
        Criteria criteria = ((Session)em.getDelegate()).createCriteria(TransferCheckout.class);
        criteria.setFirstResult(0 + (transferRequest.getPage() - 1) * 25);
        criteria.setMaxResults(25 + (transferRequest.getPage() - 1) * 25);
        if(transferRequest.getUsername() != null && !transferRequest.getUsername().equals("")) {
            criteria.add(Restrictions.eq("username",transferRequest.getUsername()));
        }
        criteria.add(Restrictions.eq("processed",transferRequest.isProcessed()));
        List<TransferCheckout> result = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(result));
    }
}
