package com.qromarck.reciperu.controllers;

import com.qromarck.reciperu.DTO.DriverDTO;
import com.qromarck.reciperu.DTO.ReniecDniDTO;
import com.qromarck.reciperu.DTO.ReportDTO;
import com.qromarck.reciperu.DTO.UserDTO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EcoRutaAPI {

    @POST("api/v1/auth/validateDni")
    Call<ApiResponse<ReniecDniDTO>> validateDNI(@Query("dni") String dni);

    @POST("api/v1/auth/register")
    Call<ApiResponse<UserDTO>> registerUser(@Body UserDTO userDTO, @Query("password") String password);

    @POST("api/v1/auth/login")
    Call<ApiResponse<Object>> login(@Body Map<String, String> requestBody);

    @POST("api/v1/auth/logout")
    Call<ApiResponse<Object>> logout(@Query("uid") String uid);

    @GET("/api/v1/users/findById/{uid}")
    Call<ApiResponse<UserDTO>> getUserByUid(@Path("uid") String uid);

    @GET("/api/v1/users/findByEmail")
    Call<ApiResponse<UserDTO>> getUserByEmail(@Query("email") String email);

    @GET("/api/v1/drivers/findByUserUid/{userId}")
    Call<ApiResponse<DriverDTO>> getDriverByUserId(@Path("userId") String userId);

    @GET("/api/v1/drivers/findByStatus/{status}")
    Call<ApiResponse<DriverDTO>> getDriverByStatus(@Path("status") String status);

    @GET("/api/v1/drivers/findByStatus/{status}/{locationId}")
    Call<ApiResponse<DriverDTO>> getDriverByStatusAndLocationId(@Path("status") String status, @Path("locationId") Long locationId);

    @POST("api/v1/reports/create")
    Call<ApiResponse<ReportDTO>> createReport(@Body ReportDTO reportDTO);

    @GET("api/v1/reports/listAllByUserId/{uid}")
    Call<ApiResponse<List<ReportDTO>>> getReportsByUserId(@Path("uid") String uid);

}