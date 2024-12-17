package com.qromarck.reciperu.services;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.qromarck.reciperu.controllers.ApiResponse;

public interface CRUD <T>{
    void create(T entity, OnSuccessListener<ApiResponse<T>> successListener, OnFailureListener failureListener);
    //void update(T entity, OnSuccessListener<ApiResponse> successListener, OnFailureListener failureListener);
    void delete(T entity, OnSuccessListener<ApiResponse<T>> successListener, OnFailureListener failureListener);
}
