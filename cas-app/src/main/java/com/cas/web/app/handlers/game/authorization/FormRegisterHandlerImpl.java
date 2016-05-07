package com.cas.web.app.handlers.game.authorization;

import com.cas.spring.entity.User;
import com.cas.web.app.Server;
import com.cas.web.app.handlers.game.authorization.FormRegisterHandler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Created by tolga on 19.02.2016.
 */
public class FormRegisterHandlerImpl implements FormRegisterHandler {

    private String usernameParam;
    private String passwordParam;
    private String returnUrl;

    public FormRegisterHandlerImpl(String defaultUsernameParam, String defaultPasswordParam, String defaultReturnUrlParam) {
        this.usernameParam = defaultUsernameParam;
        this.passwordParam = defaultPasswordParam;
    }
    public FormRegisterHandler setPasswordParam(String passwordParam){
        this.passwordParam = passwordParam;
        return this;
    }

    @Override
    public FormRegisterHandler setReturnURLParam(String returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }

    public FormRegisterHandler setUsernameParam(String usernameParam){
        this.usernameParam = usernameParam;
        return this;
    }

    public void handle(RoutingContext context) {
        HttpServerRequest req = context.request();
        if(req.method() != HttpMethod.POST) {
            context.fail(405);
        }else{
            if(!req.isExpectMultipart()) {
                throw new IllegalStateException("Form body not parsed - do you forget to include a BodyHandler?");
            }
            MultiMap params = req.formAttributes();
            String username = params.get(this.usernameParam);
            String password = params.get(this.passwordParam);
            String email    = params.get("email");
            String mobile_phone = params.get("phone_number");
            if(username != null && password != null) {
                SessionFactory entityManagerFactory = Server.factory;
                Session em = entityManagerFactory.openSession();
                em.getTransaction().begin();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setPhone_number(mobile_phone);
                user.setEmail(email);
                em.persist(user);
                em.getTransaction().commit();
                em.close();
                this.doRedirect(context.response(),this.returnUrl + "?context=RegisterSuccess");
                return;
            }
        }

    }
    private void doRedirect(HttpServerResponse response, String url) {
        response.putHeader("location", url).setStatusCode(302).end();
    }

}
