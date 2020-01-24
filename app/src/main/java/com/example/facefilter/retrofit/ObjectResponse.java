package com.example.facefilter.retrofit;

public class ObjectResponse<T> {
    private String success;
    private String message;


    public String getMessage() {
        return message;
    }

    public String getSuccess() {
        return success;
    }
}
