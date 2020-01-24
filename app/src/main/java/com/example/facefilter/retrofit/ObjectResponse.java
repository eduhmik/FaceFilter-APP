package com.example.facefilter.retrofit;

public class ObjectResponse<T> {
    private String success;
    private String message;
    private String token;


    public String getMessage() {
        return message;
    }

    public String getSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }
}
