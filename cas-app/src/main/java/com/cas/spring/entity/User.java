package com.cas.spring.entity;

import com.cas.util.HashUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;

import javax.persistence.*;
import java.util.Iterator;

/**
 * Created by tolga on 12.03.2016.
 */
@Entity
@Table(name="users")
public class User extends AbstractUser {

    @Id
    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String password_salt;

    @Column
    private double cash = 0;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String salt = HashUtil.genSalt();
        String hashedPassword = HashUtil.hashWithSalt(password,salt);
        this.password_salt = salt;
        this.password = hashedPassword;
    }

    @Override
    protected void doIsPermitted(String permissionOrRole, Handler<AsyncResult<Boolean>> handler) {

    }

    @Override
    public JsonObject principal() {
        return null;
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {

    }

    private void hasRoleOrPermission(String roleOrPermission, String query, Handler<AsyncResult<Boolean>> resultHandler) {

    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public String getPassword_salt() {
        return password_salt;
    }
}
