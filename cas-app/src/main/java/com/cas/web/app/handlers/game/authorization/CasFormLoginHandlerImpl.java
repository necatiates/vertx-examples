package com.cas.web.app.handlers.game.authorization;

import com.cas.cache.CacheManager;
import com.cas.game.model.HapinnessStatus;
import com.cas.util.HashUtil;
import com.cas.web.app.Server;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import com.cas.spring.entity.User;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * Created by tolga on 13.03.2016.
 */
public class CasFormLoginHandlerImpl implements CasFormLoginHandler {
    private static final Logger log = LoggerFactory.getLogger(CasFormLoginHandlerImpl.class);
    private String usernameParam;
    private String passwordParam;
    private String returnURLParam;
    private String directLoggedInOKURL;
    private static final String DEFAULT_DIRECT_LOGGED_IN_OK_PAGE = "<html><body><h1>Login successful</h1></body></html>";

    public CasFormLoginHandler setUsernameParam(String usernameParam) {
        this.usernameParam = usernameParam;
        return this;
    }

    public CasFormLoginHandler setPasswordParam(String passwordParam) {
        this.passwordParam = passwordParam;
        return this;
    }

    public CasFormLoginHandler setReturnURLParam(String returnURLParam) {
        this.returnURLParam = returnURLParam;
        return this;
    }

    public CasFormLoginHandler setDirectLoggedInOKURL(String directLoggedInOKURL) {
        this.directLoggedInOKURL = directLoggedInOKURL;
        return this;
    }

    public CasFormLoginHandlerImpl(String usernameParam, String passwordParam, String returnURLParam, String directLoggedInOKURL) {
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
                User user = (User) em.get(User.class,username);
                if(user != null) {
                    String hashedStoredPwd = user.getPassword();
                    String salt = user.getPassword_salt();
                    String enteredPassword = HashUtil.hashWithSalt(password,salt);
                    if(enteredPassword.equals(hashedStoredPwd)) {
                        context.setUser(user);
                        em.getTransaction().begin();
                        user.setLastLogin(new Date());
                        em.merge(user);
                        em.persist(user);
                        em.getTransaction().commit();
                        em.close();
                    }else{
                        em.close();
                        context.fail(400);
                    }
                }else {
                  em.close();
                  this.doRedirect(req.response(),"/index.html?context=LoginFail");
                  return;
                }
                context.session().put("user",user);
                if(this.directLoggedInOKURL != null) {
                    this.doRedirect(req.response(), this.directLoggedInOKURL);
                } else {
                    req.response().end("<html><body><h1>Login successful</h1></body></html>");
                }
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
