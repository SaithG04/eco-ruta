package com.qromarck.reciperu.services;

import android.content.Context;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.qromarck.reciperu.DTO.DriverDTO;
import com.qromarck.reciperu.Utilities.ApiUtilities;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.controllers.ApiResponse;
import com.qromarck.reciperu.controllers.EcoRutaAPI;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DriverServiceImpl implements DriverService {

    private final EcoRutaAPI ecoRutaAPI;

    public DriverServiceImpl(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InterfacesUtilities.getApiBackUrl(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.ecoRutaAPI = retrofit.create(EcoRutaAPI.class);
    }

    public void getDriverByUserId(String userId, OnSuccessListener<ApiResponse<DriverDTO>> successListener, OnFailureListener failureListener) {
        Call<ApiResponse<DriverDTO>> call = ecoRutaAPI.getDriverByUserId(userId);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }

    public void getDriverByStatus(String status, OnSuccessListener<ApiResponse<DriverDTO>> successListener, OnFailureListener failureListener) {
        Call<ApiResponse<DriverDTO>> call = ecoRutaAPI.getDriverByStatus(status);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }

    public void getDriverByStatusAndLocationId(String status, Long locationId, OnSuccessListener<ApiResponse<DriverDTO>> successListener, OnFailureListener failureListener) {
        Call<ApiResponse<DriverDTO>> call = ecoRutaAPI.getDriverByStatusAndLocationId(status, locationId);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }
}
