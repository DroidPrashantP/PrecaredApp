package com.app.precared.models;

/**
 * Created by prashant on 19/7/16.
 */
public class Login {
    public String name;
    public String email;
    public String password;
    public String number;
    public String referral_code;

    public Login(String name, String email, String password, String number, String referral_code) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
        this.referral_code = referral_code;
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

}