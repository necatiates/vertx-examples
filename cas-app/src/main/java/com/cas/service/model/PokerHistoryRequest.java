package com.cas.service.model;

/**
 * Created by Administrator on 21.04.2016.
 */
public class PokerHistoryRequest {
    private int page;
    private String username;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
