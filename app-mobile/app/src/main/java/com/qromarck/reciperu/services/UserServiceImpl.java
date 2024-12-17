// UserService.java
package com.qromarck.reciperu.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.qromarck.reciperu.DTO.ReniecDniDTO;
import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.Utilities.ApiUtilities;
import com.qromarck.reciperu.Utilities.ErrorUtilities;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.callbacks.UserDatabaseCallback;
import com.qromarck.reciperu.callbacks.UserProfileImageCallback;
import com.qromarck.reciperu.controllers.ApiResponse;
import com.qromarck.reciperu.controllers.EcoRutaAPI;
import com.qromarck.reciperu.interfaces.LoginUI;
import com.qromarck.reciperu.interfaces.MainMenuUI;
import com.qromarck.reciperu.interfaces.RegisterUserUI;
import com.qromarck.reciperu.interfaces.TransitionUI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserServiceImpl implements UserService{

    private final EcoRutaAPI ecoRutaAPI;

    public UserServiceImpl(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InterfacesUtilities.getApiBackUrl(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.ecoRutaAPI = retrofit.create(EcoRutaAPI.class);
    }

    @Override
    public void create(UserDTO userForRegister, String password, OnSuccessListener<ApiResponse<UserDTO>> successListener, OnFailureListener failureListener) {
        Call<ApiResponse<UserDTO>> call = ecoRutaAPI.registerUser(userForRegister, password);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }


    /*@Override
    public void update(UserDTO user, OnSuccessListener<ApiResponse> successListener, OnFailureListener failureListener){

    }

    @Override
    public void delete(UserDTO user, OnSuccessListener<ApiResponse> successListener, OnFailureListener failureListener){

    }*/

    @Override
    public void getUserByEmail(String email, OnSuccessListener<ApiResponse<UserDTO>> successListener, OnFailureListener failureListener) {
        Call<ApiResponse<UserDTO>> call = ecoRutaAPI.getUserByEmail(email);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }

    @Override
    public void getUserByUid(String uid, OnSuccessListener<ApiResponse<UserDTO>> successListener, OnFailureListener failureListener) {
        Call<ApiResponse<UserDTO>> call = ecoRutaAPI.getUserByUid(uid);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }

    public void validateDNI(String dni, OnSuccessListener<ApiResponse<ReniecDniDTO>> successListener, OnFailureListener failureListener) {
        Call<ApiResponse<ReniecDniDTO>> call = ecoRutaAPI.validateDNI(dni);
        ApiUtilities.handleResponse(call, successListener, failureListener);
    }

    @Override
    public void uploadProfileImageToStorage(String imageString, UserProfileImageCallback callback) {
        // Convierte la imagen en base64 a un bitmap
        /*byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap profileImage = InterfacesUtilities.convertToBitmap(decodedString);

        if (profileImage != null) {
            // Generar un nombre único para la imagen
            String fileName = "profile_images/" + System.currentTimeMillis() + ".jpg";

            // Subir la imagen a Firebase Storage
            StorageReference storageRef = storage.getReference().child(fileName);
            storageRef.putBytes(decodedString)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Obtener la URL de la imagen subida
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            callback.onUploadSuccess(imageUrl);
                        }).addOnFailureListener(e -> {
                            callback.onError("Error getting image URL: " + e.getMessage());
                        });
                    })
                    .addOnFailureListener(e -> {
                        callback.onError("Error uploading image: " + e.getMessage());
                    });
        } else {
            callback.onError("Error decoding image");
        }*/
    }

    public void getUserProfileImage(String userId, UserProfileImageCallback callback) {
        /*Call<UserDTO> call = ecoRutaAPI.getUserProfileImage(userId);

        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String imageString = response.body().getPhotoUrl();
                    if (imageString != null && !imageString.isEmpty()) {
                        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
                        Bitmap profileImage = InterfacesUtilities.convertToBitmap(decodedString);
                        if (profileImage != null) {
                            Glide.with(imageView).load(profileImage).into(imageView);
                            callback.onFetchSuccess(imageString);
                        } else {
                            callback.onError("Error al decodificar la imagen de perfil");
                        }
                    } else {
                        callback.onError("No se encontró imagen de perfil");
                    }
                } else {
                    callback.onError("Error al obtener la imagen de perfil: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                callback.onError("Error de red: " + t.getMessage());
            }
        });*/
    }
}
