package com.dreamfactory.sampleapp.api.services;

import com.dreamfactory.sampleapp.models.ResponseDF;
import com.dreamfactory.sampleapp.models.ResponseDelete;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by subir on 3/4/17.
 */

public interface ApiInterface {
    @GET("db/_table/Identity")
    Call<ResponseDF> getDataDF();

    @POST("db/_table/Identity")
    Call<ResponseDF> postDataDF(@Body ResponseDF classInstance);

    @DELETE("db/_table/Identity/{id}")
    Call<ResponseDelete> deleteRecord(@Path("id") int recordID);
}
