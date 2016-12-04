package com.narwadi.saveyouraccount.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by DEKSTOP on 03/12/2016.
 */
public class Account extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    private String email;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
