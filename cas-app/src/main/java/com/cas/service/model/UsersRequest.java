package com.cas.service.model;

/**
 * Created by Administrator on 20.04.2016.
 */
public class UsersRequest {
    private String username;
    private int page;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
