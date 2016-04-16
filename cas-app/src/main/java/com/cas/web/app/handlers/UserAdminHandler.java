package com.cas.web.app.handlers;

import com.cas.spring.entity.Cash;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by tolga on 15.03.2016.
 */
public class UserAdminHandler {
    public static void getUsers(RoutingContext routingContext){
        EntityManager em = Server.factory.createEntityManager();
        CriteriaQuery<User> criteria = em.getCriteriaBuilder().createQuery(User.class);
        criteria.select(criteria.from(User.class));
        List<User> userList = em.createQuery(criteria).getResultList();
        JsonArray usersArray = new JsonArray();
        for(User user : userList){
            JsonObject obj = new JsonObject();
            obj.put("username",user.getUsername());
            obj.put("cash",user.getCash());
            usersArray.add(obj);
        }
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(usersArray.encodePrettily());
        em.close();
    }

    public static void updateuser(RoutingContext routingContext) {
        EntityManager em = Server.factory.createEntityManager();
        User user = Json.decodeValue(routingContext.getBodyAsString(),User.class);
        User persistedUser= em.find(User.class,user.getUsername());
        persistedUser.setCash(user.getCash());
        em.getTransaction().begin();
        em.merge(persistedUser);
        em.getTransaction().commit();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(user));
        em.close();
    }

}
