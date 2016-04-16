package com.cas.web.app.handlers;

import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.persistence.EntityManager;

/**
 * Created by tolga on 13.03.2016.
 */
public class InfoServiceHandler {
    public static void getInfo(RoutingContext routingContext){
        User user = routingContext.session().get("user");
        EntityManager em = Server.factory.createEntityManager();
        JsonObject response = new JsonObject();
        response.put("cash",em.find(User.class,user.getUsername()).getCash());
        response.put("username",user.getUsername());
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        em.close();
        return;
    }
}
