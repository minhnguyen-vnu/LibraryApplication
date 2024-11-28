package com.jmc.library.Account;

/**
 * Abstract class for managing the account information.
 */
public abstract class Account {
    protected String username;
    protected String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract boolean isAdmin();
}
