package com.qromarck.reciperu.services;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.qromarck.reciperu.DTO.ReportDTO;
import com.qromarck.reciperu.controllers.ApiResponse;

import java.util.List;

public interface ReportService extends  CRUD<ReportDTO> {
    void getReportsByUserId(String uid, OnSuccessListener<ApiResponse<List<ReportDTO>>> successListener, OnFailureListener failureListener);
}
