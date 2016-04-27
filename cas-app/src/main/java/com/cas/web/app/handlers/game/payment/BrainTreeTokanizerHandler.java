package com.cas.web.app.handlers.game.payment;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by tolga on 02.04.2016.
 */
public class BrainTreeTokanizerHandler {

    private static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "hk6w4t8sj385cgnm",
            "zrcw3ychr9x8rdq9",
            "a13ff5859ae27030eac3e6e5b5bd565b"
    );
    public static void  getToken(RoutingContext routingContext){
        JsonObject response = new JsonObject();
        response.put("token",gateway.clientToken().generate());
        routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
        return;
    }
}
