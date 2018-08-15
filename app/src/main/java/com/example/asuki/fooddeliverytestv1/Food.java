package com.example.asuki.fooddeliverytestv1;

/**
 * Created by Asuki on 2017. 11. 25..
 */

public class Food {

    private String code;
    private String name;

    public Food(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
