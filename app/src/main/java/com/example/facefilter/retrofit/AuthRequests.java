package com.example.facefilter.retrofit;

import com.example.facefilter.model.Auth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthRequests {
    @FormUrlEncoded
    @POST("app/authenticate")
    @Headers({ "Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json" })
    Call<ObjectResponse<Auth>> authenticateAPI(
            @Field("apiKey") String apiKey
    );
}
