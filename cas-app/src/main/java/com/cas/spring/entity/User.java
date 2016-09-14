package com.cas.spring.entity;

import com.cas.util.HashUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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
    private String email;

    @Column
    private String phone_number;

    @Column
    private Date lastLogin;

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
        Double toBeTruncated = new Double(cash);

        Double truncatedDouble = new BigDecimal(toBeTruncated)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        this.cash = truncatedDouble;
    }

    public String getPassword_salt() {
        return password_salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}
