package com.qromarck.reciperu.services;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qromarck.reciperu.Entity.Location;

public class LocationServiceImpl {
    private final DatabaseReference databaseReference;

    public LocationServiceImpl() {
        // Inicializar la referencia a Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("user_locations");
    }

    public void saveLocation(Location location) {
        // Guardar la ubicación en Firebase usando el userId como clave
        databaseReference.child(location.getUserId()).setValue(location)
                .addOnSuccessListener(aVoid -> {
                    // Éxito al guardar la ubicación
                    Log.d("LocationService", "Ubicación guardada correctamente en Firebase.");
                })
                .addOnFailureListener(e -> {
                    // Error al guardar la ubicación
                    Log.e("LocationService", "Error al guardar la ubicación en Firebase.", e);
                });
    }
}
