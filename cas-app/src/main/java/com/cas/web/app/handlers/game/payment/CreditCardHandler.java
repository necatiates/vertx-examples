package com.cas.web.app.handlers.game.payment;

import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import com.twocheckout.Twocheckout;
import com.twocheckout.TwocheckoutCharge;
import com.twocheckout.model.Authorization;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.HashMap;

/**
 * Created by tolga on 05.04.2016.
 */
public class CreditCardHandler {
    public static void handleCardOK(RoutingContext context){
        Twocheckout.privatekey = "2CE86A71-FC0D-48FB-B1F0-54AF6C118A90";
        Twocheckout.mode = "sandbox";

        try {
            HashMap billing = new HashMap();
            billing.put("name", "Testing Tester");
            billing.put("addrLine1", "xvxcvxcvxcvcx");
            billing.put("city", "Columbus");
            billing.put("state", "Ohio");
            billing.put("country", "USA");
            billing.put("zipCode", "43230");
            billing.put("email", "tester@2co.com");
            billing.put("phoneNumber", "555-555-5555");

            HashMap request = new HashMap();
            request.put("sellerId", "901313892");
            request.put("merchantOrderId", "test123");
            request.put("token", context.request().formAttributes().get("token"));
            request.put("currency", "USD");
            request.put("total", context.request().formAttributes().get("amount_card"));
            request.put("billingAddr", billing);

            Authorization response = TwocheckoutCharge.authorize(request);
            String message = response.getResponseMsg();
            Session em = Server.factory.openSession();
            User user = (User) em.get(User.class,((User)context.session().get("user")).getUsername());
            em.getTransaction().begin();
            user.setCash(user.getCash() + Double.parseDouble(context.request().formAttributes().get("amount_card")));
            em.merge(user);
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            String message = e.toString();
        }
        context.request().response().putHeader("location", "/private/games.html").setStatusCode(302).end();
    }
}
