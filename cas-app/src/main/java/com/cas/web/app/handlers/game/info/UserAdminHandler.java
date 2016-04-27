package com.cas.web.app.handlers.game.info;

import com.cas.service.model.UsersRequest;
import com.cas.spring.entity.Cash;
import com.cas.spring.entity.TransferCheckin;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by tolga on 15.03.2016.
 */
public class UserAdminHandler {
    public static void getUsers(RoutingContext routingContext){
        final UsersRequest usersRequest = Json.decodeValue(routingContext.getBodyAsString(),UsersRequest.class);
        Session em = Server.factory.openSession();
        Criteria criteria = em.createCriteria(User.class);
        criteria.setFirstResult(0 + (usersRequest.getPage() - 1) * 25);
        criteria.setMaxResults(25 + (usersRequest.getPage() - 1) * 25);
        if(usersRequest.getUsername() != null && !usersRequest.getUsername().equals("")) {
            criteria.add(Restrictions.eq("username",usersRequest.getUsername()));
        }
        JsonArray usersArray = new JsonArray();
        List<User> userList = criteria.list();
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
        Session em = Server.factory.openSession();
        User user = Json.decodeValue(routingContext.getBodyAsString(),User.class);
        User persistedUser= (User) em.get(User.class,user.getUsername());
        persistedUser.setCash(user.getCash());
        em.getTransaction().begin();
        em.merge(persistedUser);
        em.getTransaction().commit();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(user));
        em.close();
    }

}
