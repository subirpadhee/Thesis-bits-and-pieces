package com.dreamfactory.sampleapp.api;

import android.util.Log;

import com.dreamfactory.sampleapp.DreamFactoryApp;
import com.dreamfactory.sampleapp.models.ErrorMessage;
import com.dreamfactory.sampleapp.utils.PrefUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static android.content.ContentValues.TAG;
import static com.dreamfactory.sampleapp.DreamFactoryApp.API_KEY;

/**
 * Created by Nirmel on 6/3/2016.
 *
 * Componenet responsibe for handling services and api calls
 */
public class DreamFactoryAPI {

    private static DreamFactoryAPI INSTANCE;

    private Retrofit retrofit;

    private OkHttpClient httpClient;

    private static Converter<ResponseBody, ErrorMessage> errorConverter;

    public static String testToken;

    public static Boolean runningFromTest = false;

    private DreamFactoryAPI() {
        httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder ongoing = chain.request().newBuilder();

                if(API_KEY == null) {
                    Log.w(DreamFactoryAPI.class.getSimpleName(), "API key not provided");
                } else {
                    ongoing.addHeader("X-DreamFactory-Api-Key", API_KEY);
                    Log.d(TAG, "API KEY: "+API_KEY);
                }

                if(!runningFromTest) {
                    String token = PrefUtil.getString(DreamFactoryApp.getAppContext(), DreamFactoryApp.SESSION_TOKEN);

                    if (token != null && !token.isEmpty()) {
                        ongoing.addHeader("X-DreamFactory-Session-Token", token);
                        Log.d(TAG, "TOKEN: "+token);
                    }
                } else if(testToken != null){
                    ongoing.addHeader("X-DreamFactory-Session-Token", testToken);
                }

                return chain.proceed(ongoing.build());
            }
        }).build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        retrofit = new Retrofit.Builder()
                .baseUrl(DreamFactoryApp.INSTANCE_URL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        errorConverter = retrofit.responseBodyConverter(ErrorMessage.class, new Annotation[0]);
    }

    public static ErrorMessage getErrorMessage(Response response) {
        ErrorMessage error = null;

        try {
            error = errorConverter.convert(response.errorBody());
        } catch (IOException e) {
            error = new ErrorMessage("Unexpected error");

            Log.e("ERROR", "Unexpected error while serialising error message", e);
        }

        return error;
    }

    public synchronized static DreamFactoryAPI getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DreamFactoryAPI();
        }

        return INSTANCE;
    }

    public <T> T getService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
