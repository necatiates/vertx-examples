package com.cas.spring.entity;

import com.cas.util.HashUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 18.05.2016.
 */
@Entity
@Table(name="admins")
public class Admin extends AbstractUser {

    @Id
    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String password_salt;

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
    protected void doIsPermitted(String s, Handler<AsyncResult<Boolean>> handler) {

    }

    @Override
    public JsonObject principal() {
        return null;
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {

    }

    public String getPassword_salt() {
        return password_salt;
    }

    public void setPassword_salt(String password_salt) {
        this.password_salt = password_salt;
    }
}
