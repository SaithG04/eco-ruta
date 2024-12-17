package com.qromarck.reciperu.services;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.callbacks.UserDatabaseCallback;
import com.qromarck.reciperu.callbacks.UserProfileImageCallback;
import com.qromarck.reciperu.controllers.ApiResponse;

public interface UserService {
    void create(UserDTO userDTO, String password, OnSuccessListener<ApiResponse<UserDTO>> successListener, OnFailureListener failureListener);
    void getUserByEmail(String email, OnSuccessListener<ApiResponse<UserDTO>> successListener, OnFailureListener failureListener);
    void getUserByUid(String uid, OnSuccessListener<ApiResponse<UserDTO>> successListener, OnFailureListener failureListener);
    void getUserProfileImage(String userId, UserProfileImageCallback callback);
    void uploadProfileImageToStorage(String imageString, UserProfileImageCallback callback);
}
