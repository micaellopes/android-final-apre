package com.example.andrey.academiaand.model;

/**
 * Created by Andrey on 11/06/2017.
 */

public class ResultLogin {

    private boolean success;
    private String message;
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString() {
        return message + " - " + token;
    }
}
