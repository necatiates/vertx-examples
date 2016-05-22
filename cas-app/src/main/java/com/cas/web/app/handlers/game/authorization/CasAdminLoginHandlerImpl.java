package com.cas.web.app.handlers.game.authorization;


import com.cas.StaticDefinitions;
import com.cas.spring.entity.Admin;
import com.cas.spring.entity.User;
import com.cas.util.HashUtil;
import com.cas.web.app.Server;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;
import org.jasypt.util.text.StrongTextEncryptor;

import java.util.Date;

/**
 * Created by tolga on 13.03.2016.
 */
public class CasAdminLoginHandlerImpl implements CasAdminLoginHandler {
    private static final Logger log = LoggerFactory.getLogger(CasAdminLoginHandlerImpl.class);
    private String usernameParam;
    private String passwordParam;
    private String returnURLParam;
    private String directLoggedInOKURL;
    private static final String DEFAULT_DIRECT_LOGGED_IN_OK_PAGE = "<html><body><h1>Login successful</h1></body></html>";

    public CasAdminLoginHandler setUsernameParam(String usernameParam) {
        this.usernameParam = usernameParam;
        return this;
    }

    public CasAdminLoginHandler setPasswordParam(String passwordParam) {
        this.passwordParam = passwordParam;
        return this;
    }

    public CasAdminLoginHandler setReturnURLParam(String returnURLParam) {
        this.returnURLParam = returnURLParam;
        return this;
    }

    public CasAdminLoginHandler setDirectLoggedInOKURL(String directLoggedInOKURL) {
        this.directLoggedInOKURL = directLoggedInOKURL;
        return this;
    }

    public CasAdminLoginHandlerImpl(String usernameParam, String passwordParam, String returnURLParam, String directLoggedInOKURL) {
        this.usernameParam = usernameParam;
        this.passwordParam = passwordParam;
        this.returnURLParam = returnURLParam;
        this.directLoggedInOKURL = directLoggedInOKURL;
    }

    public void handle(RoutingContext context) {
        HttpServerRequest req = context.request();
        if(req.method() != HttpMethod.POST) {
            context.fail(405);
        } else {
            if(!req.isExpectMultipart()) {
                throw new IllegalStateException("Form body not parsed - do you forget to include a BodyHandler?");
            }

            MultiMap params = req.formAttributes();
            String username = params.get(this.usernameParam);
            String password = params.get(this.passwordParam);
            if(username != null && password != null) {
                org.hibernate.SessionFactory entityManagerFactory = Server.factory;
                Session em = entityManagerFactory.openSession();
                Admin admin = (Admin) em.get(Admin.class,username);
                if(admin != null) {
                    String hashedStoredPwd = admin.getPassword();
                    String salt = admin.getPassword_salt();
                    String enteredPassword = HashUtil.hashWithSalt(password,salt);
                    if(enteredPassword.equals(hashedStoredPwd)) {
                        context.setUser(admin);
                        em.close();
                    }else{
                        em.close();
                        context.fail(400);
                    }
                }else {
                  em.close();
                  this.doRedirect(req.response(),"/casadmin/login.html?context=LoginFail");
                  return;
                }
                context.session().put(StaticDefinitions.ADMIN_SESSION_KEY,admin);
                this.doRedirect(req.response(),"/casadmin/index.html?context=LoginSuccess");
            } else {
                log.warn("No username or password provided in form - did you forget to include a BodyHandler?");
                context.fail(400);
            }
        }

    }

    private void doRedirect(HttpServerResponse response, String url) {
        response.putHeader("location", url).setStatusCode(302).end();
    }
}
