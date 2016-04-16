package com.cas.web.app.handlers;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.impl.FormLoginHandlerImpl;

/**
 * Created by tolga on 13.03.2016.
 */
@VertxGen

public interface CasFormLoginHandler extends Handler<RoutingContext> {
    String DEFAULT_USERNAME_PARAM = "username";
    String DEFAULT_PASSWORD_PARAM = "password";
    String DEFAULT_RETURN_URL_PARAM = "return_url";

    static CasFormLoginHandler create() {
        return new CasFormLoginHandlerImpl("username", "password", "return_url", (String)null);
    }

    static CasFormLoginHandler create(String usernameParam, String passwordParam, String returnURLParam, String directLoggedInOKURL) {
        return new CasFormLoginHandlerImpl(usernameParam, passwordParam, returnURLParam, directLoggedInOKURL);
    }

    @Fluent
    CasFormLoginHandler setUsernameParam(String var1);

    @Fluent
    CasFormLoginHandler setPasswordParam(String var1);

    @Fluent
    CasFormLoginHandler setReturnURLParam(String var1);

    @Fluent
    CasFormLoginHandler setDirectLoggedInOKURL(String var1);
}
