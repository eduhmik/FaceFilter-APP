package com.example.facefilter.retrofit;

import com.example.facefilter.model.TrendingFilters;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface FiltersRequests {
    @GET("vr/trending")
    @Headers({ "Content-Type: application/x-www-form-urlencoded; charset=UTF-8", "Accept: application/json" })
    Call<ListResponse<TrendingFilters>> getTrendingFilters(
            @Header("Authorization") String token
    );
}
