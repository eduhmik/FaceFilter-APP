package com.example.facefilter.model;

public class Auth {
    private String success;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    private String message;
    private String token;
    private String expiresIn;
}
