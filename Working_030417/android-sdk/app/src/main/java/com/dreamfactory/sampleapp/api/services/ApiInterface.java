package com.dreamfactory.sampleapp.api.services;

import com.dreamfactory.sampleapp.models.ResponseDF;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by subir on 3/4/17.
 */

public interface ApiInterface {
    @GET("db/_table/Identity")
    Call<ResponseDF> getDataDF();
}
