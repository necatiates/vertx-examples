package com.cas.web.app.handlers.game.payment;

import com.braintreegateway.*;
import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

/**
 * Created by tolga on 02.04.2016.
 */
public class BrainTreeCheckoutHandler {
    private static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "hk6w4t8sj385cgnm",
            "zrcw3ychr9x8rdq9",
            "a13ff5859ae27030eac3e6e5b5bd565b"
    );

    public static void checkout(RoutingContext routingContext){
        String nonceFromTheClient = routingContext.request().formAttributes().get("payment_method_nonce");
        TransactionRequest request = new TransactionRequest()
                .amount(new BigDecimal("10.00"))
                .paymentMethodNonce(nonceFromTheClient)
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        if(result.isSuccess()){
            Session em = Server.factory.openSession();
            User user = (User) em.get(User.class,((User)routingContext.session().get("user")).getUsername());
            em.getTransaction().begin();
            user.setCash(user.getCash() + result.getTarget().getAmount().doubleValue());
            em.persist(user);
            em.getTransaction().commit();
            em.close();
        }
        routingContext.request().response().putHeader("location", "/private/index.html").setStatusCode(302).end();
        return;
    }
}
