package com.example.asuki.fooddeliverytestv1;

/**
 * Created by Asuki on 2017. 12. 03..
 */

public class Clients {
    private String name;
    private String address;
    private String mobile;
    private String email;

    public Clients(String name, String address, String mobile, String email) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
