package com.narwadi.saveyouraccount.model;

import java.io.Serializable;

/**
 * Created by DEKSTOP on 05/12/2016.
 */
public class UserModel implements Serializable {
    private int id;
    private String username;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        this.password = password;
    }
}
