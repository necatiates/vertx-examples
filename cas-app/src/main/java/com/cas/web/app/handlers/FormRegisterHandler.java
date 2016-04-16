package com.cas.web.app.handlers;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by tolga on 19.02.2016.
 */
@VertxGen
public interface FormRegisterHandler extends Handler<RoutingContext> {
    String DEFAULT_USERNAME_PARAM = "username";
    String DEFAULT_PASSWORD_PARAM = "password";
    String DEFAULT_RETURN_URL_PARAM = "return_url";

    static FormRegisterHandler create() {
        return new FormRegisterHandlerImpl(DEFAULT_USERNAME_PARAM, DEFAULT_PASSWORD_PARAM, DEFAULT_RETURN_URL_PARAM);
    }

    @Fluent
    FormRegisterHandler setUsernameParam(String var1);

    @Fluent
    FormRegisterHandler setPasswordParam(String var1);

    @Fluent
    FormRegisterHandler setReturnURLParam(String var1);
}
