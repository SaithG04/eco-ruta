package com.qromarck.reciperu.services;

import android.content.Context;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.qromarck.reciperu.DTO.ReportDTO;
import com.qromarck.reciperu.Utilities.ApiUtilities;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.controllers.ApiResponse;
import com.qromarck.reciperu.controllers.EcoRutaAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportServiceImpl implements ReportService {

    private final EcoRutaAPI ecoRutaAPI;

    public ReportServiceImpl(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InterfacesUtilities.getApiBackUrl(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.ecoRutaAPI = retrofit.create(EcoRutaAPI.class);
    }

    @Override
    public void create(ReportDTO reportDTO, OnSuccessListener<ApiResponse<ReportDTO>> successListener, OnFailureListener failureListener) {
        Call<ApiResponse<ReportDTO>> call = ecoRutaAPI.createReport(reportDTO);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }

    @Override
    public void delete(ReportDTO reportDTO, OnSuccessListener<ApiResponse<ReportDTO>> successListener, OnFailureListener failureListener) {
        //Call<ApiResponse> call = ecoRutaAPI.deleteReport(reportDTO);
        //ApiUtilities.handleResponse(call, successListener, failureListener);
    }

    @Override
    public void getReportsByUserId(String uid, OnSuccessListener<ApiResponse<List<ReportDTO>>> successListener, OnFailureListener failureListener) {
        Call<ApiResponse<List<ReportDTO>>> call = ecoRutaAPI.getReportsByUserId(uid);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }
}
