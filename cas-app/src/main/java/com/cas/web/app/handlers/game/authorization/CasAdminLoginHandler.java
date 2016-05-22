package com.cas.web.app.handlers.game.authorization;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by tolga on 13.03.2016.
 */

public interface CasAdminLoginHandler extends Handler<RoutingContext> {
    String DEFAULT_USERNAME_PARAM = "username";
    String DEFAULT_PASSWORD_PARAM = "password";
    String DEFAULT_RETURN_URL_PARAM = "return_url";

    static CasAdminLoginHandler create() {
        return new CasAdminLoginHandlerImpl("username", "password", "return_url", (String)null);
    }

    static CasAdminLoginHandler create(String usernameParam, String passwordParam, String returnURLParam, String directLoggedInOKURL) {
        return new CasAdminLoginHandlerImpl(usernameParam, passwordParam, returnURLParam, directLoggedInOKURL);
    }


    CasAdminLoginHandler setUsernameParam(String var1);

    CasAdminLoginHandler setPasswordParam(String var1);


    CasAdminLoginHandler setReturnURLParam(String var1);


    CasAdminLoginHandler setDirectLoggedInOKURL(String var1);
}
