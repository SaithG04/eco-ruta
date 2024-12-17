package com.qromarck.reciperu.interfaces;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.Utilities.RealTimeLocationUpdater;

import java.util.Objects;

//SONIDO DE PROXIMIDAD


public class MapUI extends AppCompatActivity implements OnMapReadyCallback, LocationListener, ValueEventListener{

    private GoogleMap googleMap;
    private boolean mapReady = false;
    private AlertDialog dialog;
    public static final int REQUEST_LOCATION_PERMISSION = 1;
    private Location lastKnownLocation;
    private Marker driverMarker;

    private String driverId;

    private RealTimeLocationUpdater realTimeLocationUpdater;

    private boolean modUser = false;

    private DatabaseReference otherUserLocationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reci_maps_ui);

        initializeUI();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        mapReady = true;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        googleMap.setMyLocationEnabled(true);

        realTimeLocationUpdater.setLocationCallback(userLocation -> {
            if (userLocation != null && mapReady) {
                Log.d("Map", "Moviendo la cámara a la ubicación actual.");
                LatLng currentLocation = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            } else {
                Log.e("Map", "Ubicación no válida o mapa no está listo.");
            }
        });

        // Iniciar actualizaciones de ubicación
        realTimeLocationUpdater.startLocationUpdates(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (realTimeLocationUpdater != null) {
            realTimeLocationUpdater.startLocationUpdates(this);
        }
        if (modUser) getConductorUbication();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (realTimeLocationUpdater != null) {
            realTimeLocationUpdater.startLocationUpdates(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        destroyDialog();
        if (realTimeLocationUpdater != null) {
            realTimeLocationUpdater.stopLocationUpdates();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        destroyDialog();
        if (realTimeLocationUpdater != null) {
            realTimeLocationUpdater.stopLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyDialog();

        if (realTimeLocationUpdater != null) {
            realTimeLocationUpdater.stopLocationUpdates();
        }
        stopListeningToLocation();
        Log.d("DEBUG", "FROM: " + MapUI.class.getSimpleName());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("DEBUG", "FROM: " + MapUI.class.getSimpleName());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //updateLastUbication();
            } else {
                finish();
                startActivity(new Intent(MapUI.this, MainMenuUI.class));
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lastKnownLocation = location;
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        if (!isFinishing() && !isDestroyed()) {
            destroyDialog();
        }
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        if (!isFinishing() && !isDestroyed()) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                if (dialog == null || !dialog.isShowing()) {
                    dialog = new AlertDialog.Builder(this)
                            .setMessage("Por favor, habilite su ubicación para continuar.")
                            .setPositiveButton("Configuración", (dialog, which) -> {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            })
                            .setNegativeButton("Cancelar", (dialog, which) -> {
                                TransitionUI.destino = MainMenuUI.class;
                                startActivity(new Intent(MapUI.this, TransitionUI.class));
                                finish();
                            })
                            .setCancelable(false) // Evita que se cierre tocando fuera del diálogo
                            .show();
                }
            } else {
                System.out.println(provider);
            }
        }
    }

    /*private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }*/

    private void getConductorUbication() {
        //String driverId = "2PEUsE6tkVRlvsm9KmVHVxutmBw1";
        otherUserLocationRef = FirebaseDatabase.getInstance().getReference("user_locations").child(driverId);
        otherUserLocationRef.addValueEventListener(this);
    }

    // Método para eliminar el listener cuando la actividad ya no está visible
    private void stopListeningToLocation() {
        if (otherUserLocationRef != null) {
            otherUserLocationRef.removeEventListener(this);
        }
    }

    private void setMarkerOfConductor(double latitude, double longitude) {
        if (googleMap != null) {
            LatLng otherUserLocation = new LatLng(latitude, longitude);
            // Cargar el icono personalizado desde los recursos
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.truck_icon);
            //BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(otherUserLocation)
                    .title("CONDUCTOR")
                    .icon(icon); // Personalizar el icono del marcador

            // Elimina el marcador anterior del otro usuario si ya existe
            if (driverMarker != null) {
                driverMarker.remove();
            }

            // Agrega el nuevo marcador del otro usuario en el mapa
            driverMarker = googleMap.addMarker(markerOptions);
        }
    }

    private void destroyDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void initializeUI() {
        try {
            realTimeLocationUpdater = new RealTimeLocationUpdater(this);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            Objects.requireNonNull(mapFragment).getMapAsync(this);

            driverId = InterfacesUtilities.getDriverId(this);
            if(driverId != null){
                modUser = true;
            }

        }catch (Exception e){
            Log.e("DEBUG", Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        try {
            Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
            Double longitude = dataSnapshot.child("longitude").getValue(Double.class);

            if (latitude != null && longitude != null) {
                setMarkerOfConductor(latitude, longitude);
            } else {
                Log.d("DEBUG", "Latitude or Longitude is null");
            }
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        // Manejar errores de lectura de datos
        Log.e("FirebaseDatabase", "Error al obtener la ubicación del conductor: " + error.getMessage());
    }
}

