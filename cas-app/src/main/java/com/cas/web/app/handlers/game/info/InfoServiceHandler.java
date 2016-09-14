package com.cas.web.app.handlers.game.info;

import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;

import javax.persistence.EntityManager;

/**
 * Created by tolga on 13.03.2016.
 */
public class InfoServiceHandler {
    public static void getInfo(RoutingContext routingContext){
        User user = routingContext.session().get("user");
        if(user == null){
            JsonObject response = new JsonObject();
            response.put("username","Unauthorized");
            routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(response));
            return;
        }
        Session em = Server.factory.openSession();
        JsonObject response = new JsonObject();
        response.put("cash",((User)em.get(User.class,user.getUsername())).getCash());
        response.put("username",user.getUsername());
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        em.close();
        return;
    }
}
