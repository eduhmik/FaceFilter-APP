package com.example.facefilter.retrofit;

public class ObjectResponse<T> {
    private String status;
    private String access_token;
    private String role;
    private String message;
    private T data;

    public String getAccess_token() {
        return access_token;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }
}
