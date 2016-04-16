package com.cas.service.model;

/**
 * Created by tolga on 09.04.2016.
 */
public class CheckoutsRequest {
    private String username;
    private int page;
    private boolean processed;

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

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
