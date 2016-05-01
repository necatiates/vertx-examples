package com.cas.web.app.handlers.game.info;

import com.cas.spring.entity.Cash;
import com.cas.spring.entity.GlobalSettings;
import com.cas.web.app.Server;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Criteria;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by tolga on 15.03.2016.
 */
public class SettingsHandler {
    public static void getSettings(RoutingContext routingContext){
        Session em = Server.factory.openSession();
        Criteria criteria = em.createCriteria(GlobalSettings.class);
        List<GlobalSettings> cashList = criteria.list();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(cashList));
        em.close();
    }

    public static void updateSettings(RoutingContext routingContext) {
        Session em = Server.factory.openSession();
        GlobalSettings cash = Json.decodeValue(routingContext.getBodyAsString(),GlobalSettings.class);
        em.getTransaction().begin();
        em.merge(cash);
        em.getTransaction().commit();
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(cash));
        em.close();

    }
}
