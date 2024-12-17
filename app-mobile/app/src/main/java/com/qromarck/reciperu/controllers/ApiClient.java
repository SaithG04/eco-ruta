package com.qromarck.reciperu.controllers;

import android.content.Context;

import com.qromarck.reciperu.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            // Obtiene la URL base desde los recursos
            String ecoRutaApiUrl = context.getString(R.string.WEB_API_URL);
            //String reniecApiUrl = context.getString(R.string.RENIEC_API_URL);

            retrofit = new Retrofit.Builder()
                    .baseUrl(ecoRutaApiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}