package com.example.facefilter.retrofit;

import java.util.ArrayList;

public class ListResponse<T> {
    public String status, message;

    public ArrayList<T> data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<T> getData() {
        return data;
    }
}
