package com.example.facefilter.retrofit;

import java.util.ArrayList;

public class ListResponse<T> {
    public String success, message;

    public ArrayList<T> media;

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<T> getMedia() {
        return media;
    }
}
