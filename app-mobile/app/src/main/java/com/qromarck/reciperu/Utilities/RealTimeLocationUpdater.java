package com.qromarck.reciperu.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.qromarck.reciperu.services.LocationServiceImpl;

import android.location.Location;
import android.util.Log;

import java.util.function.Consumer;

public class RealTimeLocationUpdater {

    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final LocationServiceImpl locationService;
    private Consumer<com.qromarck.reciperu.Entity.Location> locationConsumer;
    private LocationCallback locationCallback;


    // Método para establecer el callback del Consumer
    public void setLocationCallback(Consumer<com.qromarck.reciperu.Entity.Location> locationConsumer) {
        this.locationConsumer = locationConsumer;
    }
    public RealTimeLocationUpdater(Context context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        locationService = new LocationServiceImpl();
    }

    public void startLocationUpdates(Context context) {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(3000) // Actualiza cada 3 segundos
                .setFastestInterval(2000) // Intervalo más rápido
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos si no están concedidos
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Crear y asignar LocationCallback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    com.qromarck.reciperu.Entity.Location userLocation = new com.qromarck.reciperu.Entity.Location();
                    userLocation.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    userLocation.setLatitude(location.getLatitude());
                    userLocation.setLongitude(location.getLongitude());
                    userLocation.setCity(GeoCoderHelper.getCityFromCoordinates(context, location.getLatitude(), location.getLongitude()));

                    // Log para depurar
                    Log.d("LocationUpdater", "Lat: " + userLocation.getLatitude() + ", Lon: " + userLocation.getLongitude());

                    // Guardar la ubicación en Firebase
                    locationService.saveLocation(userLocation);

                    // Llamar al Consumer si está configurado
                    if (locationConsumer != null) {
                        locationConsumer.accept(userLocation);
                    } else {
                        Log.e("LocationUpdater", "Consumer no configurado.");
                    }
                }
            }
        };


        // Inicia las actualizaciones de ubicación
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        // Asegúrate de que locationCallback no sea nulo
        if (locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            locationCallback = null; // Limpia la referencia para evitar posibles fugas de memoria
        }
    }
}
