package com.app.precared.models;

/**
 * Created by prashant on 19/7/16.
 */
public class Login {
    public String name;
    public String email;
    public String password;
    public String number;

    public Login(String name, String email, String password, String number) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
    }



    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

}