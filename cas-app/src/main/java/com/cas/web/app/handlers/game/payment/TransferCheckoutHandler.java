package com.cas.web.app.handlers.game.payment;

import com.cas.service.model.CheckoutsRequest;
import com.cas.service.model.FlagChangeRequest;
import com.cas.spring.entity.TransferCheckout;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by tolga on 09.04.2016.
 */
public class TransferCheckoutHandler {
    public static void handle(RoutingContext routingContext){
        final TransferCheckout transferRequest = Json.decodeValue(routingContext.getBodyAsString(),TransferCheckout.class);
        transferRequest.setUsername(((User)routingContext.session().get("user")).getUsername());
        Session em = Server.factory.openSession();
        em.getTransaction().begin();
        em.persist(transferRequest);
        em.getTransaction().commit();
        em.close();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(new JsonObject().put("id",transferRequest.getId())));
    }
    public static void getTransfers(RoutingContext routingContext){
        final CheckoutsRequest transferRequest = Json.decodeValue(routingContext.getBodyAsString(),CheckoutsRequest.class);
        Session em = Server.factory.openSession();
        Criteria criteria = em.createCriteria(TransferCheckout.class);
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
    public static void changeFlag(RoutingContext routingContext){
        final FlagChangeRequest request =  Json.decodeValue(routingContext.getBodyAsString(),FlagChangeRequest.class);
        Session em = Server.factory.openSession();
        TransferCheckout checkout = (TransferCheckout) em.get(TransferCheckout.class,request.getId());
        checkout.setProcessed(request.isFlag());
        em.getTransaction().begin();
        em.persist(checkout);
        User user = (User) em.get(User.class,checkout.getUsername());
        user.setCash(user.getCash() - checkout.getAmount());
        em.persist(user);
        em.getTransaction().commit();
        JsonObject response = new JsonObject();
        response.put("ok","ok");
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        em.close();
    }
}
