package com.example.facefilter.retrofit;

import android.util.Log;

import com.svrf.client.ApiException;
import com.svrf.client.api.AuthenticateApi;
import com.svrf.client.model.AuthResponse;
import com.svrf.client.model.Body;

public class AuthenticateAPI {
    public static void main(String[] args) {

        AuthenticateApi apiInstance = new AuthenticateApi();
        Body body = new Body(); // Body |
        try {
            AuthResponse result = apiInstance.authenticate(body);
//            System.out.println(result);
            Log.e("Response", String.valueOf(result));
        } catch (ApiException e) {
            Log.e("", "Exception when calling AuthenticateApi#authenticate");
            e.printStackTrace();
        }
    }
}
