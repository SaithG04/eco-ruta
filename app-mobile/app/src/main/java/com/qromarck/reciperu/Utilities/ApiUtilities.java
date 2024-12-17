package com.qromarck.reciperu.Utilities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.qromarck.reciperu.controllers.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtilities {

    public static <T> void handleResponse(Call<ApiResponse<T>> call, OnSuccessListener<ApiResponse<T>> successListener, OnFailureListener failureListener){
        call.enqueue(new Callback<ApiResponse<T>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<T>> call, @NonNull Response<ApiResponse<T>> response) {
                if (response.body() != null){
                    successListener.onSuccess(response.body());
                }else {
                    failureListener.onFailure(ErrorUtilities.getExceptionFromResponse(response));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<T>> call, Throwable t) {
                failureListener.onFailure(new Exception(t.getMessage()));
            }
        });
    }

}
