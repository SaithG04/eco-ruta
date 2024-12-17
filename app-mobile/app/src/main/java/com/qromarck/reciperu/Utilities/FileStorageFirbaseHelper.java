package com.qromarck.reciperu.Utilities;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FileStorageFirbaseHelper {

    private static final String TAG = "FileStorageHelper";

    public static void uploadImageToFirebase(Uri photoUri, OnSuccessListener<String> onSuccessListener, OnFailureListener onFailureListener) {
        // Obtén una referencia a Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("reports/" + UUID.randomUUID().toString() + ".jpg");

        // Subir la imagen
        UploadTask uploadTask = imageRef.putFile(photoUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // La imagen se subió exitosamente
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String photoUrl = uri.toString();
                Log.d(TAG, "Imagen subida correctamente: " + photoUrl);
                onSuccessListener.onSuccess(photoUrl);
            });
        }).addOnFailureListener(e -> {
            // Maneja el error
            Log.e(TAG, "Error al subir la imagen: " + e.getMessage());
            onFailureListener.onFailure(e);
        });
    }
}
