package com.cas.web.app.handlers.game.authorization;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by tolga on 13.03.2016.
 */

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


    CasFormLoginHandler setUsernameParam(String var1);

    CasFormLoginHandler setPasswordParam(String var1);


    CasFormLoginHandler setReturnURLParam(String var1);


    CasFormLoginHandler setDirectLoggedInOKURL(String var1);
}
