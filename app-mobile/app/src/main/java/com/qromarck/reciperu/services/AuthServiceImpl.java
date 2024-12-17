package com.qromarck.reciperu.services;

import android.content.Context;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.qromarck.reciperu.Utilities.ApiUtilities;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.controllers.ApiResponse;
import com.qromarck.reciperu.controllers.EcoRutaAPI;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthServiceImpl {

    private final EcoRutaAPI ecoRutaAPI;

    public AuthServiceImpl(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InterfacesUtilities.getApiBackUrl(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.ecoRutaAPI = retrofit.create(EcoRutaAPI.class);
    }

    public void logIn(String token, OnSuccessListener<ApiResponse<Object>> successListener, OnFailureListener failureListener) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("idToken", token);

        Call<ApiResponse<Object>> call = ecoRutaAPI.login(requestBody);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }

    public void logOut(String uid, OnSuccessListener<ApiResponse<Object>> successListener, OnFailureListener failureListener) {
        Call<ApiResponse<Object>> call = ecoRutaAPI.logout(uid);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }
}
