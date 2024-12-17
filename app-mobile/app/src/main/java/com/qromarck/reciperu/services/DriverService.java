package com.qromarck.reciperu.services;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.qromarck.reciperu.DTO.DriverDTO;
import com.qromarck.reciperu.controllers.ApiResponse;

public interface DriverService {
    void getDriverByUserId(String userId, OnSuccessListener<ApiResponse<DriverDTO>> successListener, OnFailureListener failureListener);
    void getDriverByStatus(String status, OnSuccessListener<ApiResponse<DriverDTO>> successListener, OnFailureListener failureListener);
    void getDriverByStatusAndLocationId(String status, Long locationId, OnSuccessListener<ApiResponse<DriverDTO>> successListener, OnFailureListener failureListener);

}
