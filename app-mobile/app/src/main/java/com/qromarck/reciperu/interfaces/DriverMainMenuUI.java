package com.qromarck.reciperu.interfaces;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.qromarck.reciperu.DTO.DriverDTO;
import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.*;
import com.qromarck.reciperu.services.AuthServiceImpl;

public class DriverMainMenuUI extends AppCompatActivity implements LogoutDialogInterface{
    private FrameLayout loadingLayoutconductor;
    private ProgressBar loadingIndicatorconductor;
    public static String typeChange = "";

    private AuthServiceImpl authServiceImpl;

    private static boolean exit;
    private static final int REQUEST_LOCATION_PERMISSION = 1001;

    private static final String TAG = "ECO_RUTA";
    private DriverDTO localDriver;
    private UserDTO localUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conductor_ui);

        initializeDriver();


        Button verMapa = findViewById(R.id.btn_mapa_conductor);
        loadingLayoutconductor = findViewById(R.id.loadingLayoutconductor);
        loadingIndicatorconductor = findViewById(R.id.loadingIndicatorconductor);
        //ConductorUIManager.getInstance().setConductorUI(this);
        exit = true;

        verMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingIndicator();
                if (NetworkUtilities.isNetworkAvailable(getApplicationContext())) {
                    // Verifica si tienes los permisos de ubicación
                    if (ContextCompat.checkSelfPermission(DriverMainMenuUI.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // Verificar si el GPS está habilitado
                        if (isGPSEnabled()) {
                            hideLoadingIndicator();
                            // El GPS está apagado, mostrar un diálogo para pedir al usuario que lo active
                            showEnableGPSDialog();
                        } else {
                            // El GPS está habilitado, realizar la acción deseada
                            hideLoadingIndicator();
                            exit = false;
                            TransitionUI.destino = MapUI.class;
                            Log.d("DEBUG", "FROM: " + DriverMainMenuUI.class.getSimpleName());
                            startActivity(new Intent(DriverMainMenuUI.this, TransitionUI.class));
                        }
                    } else {
                        // Si no tienes los permisos, solicítalos al usuario
                        ActivityCompat.requestPermissions(DriverMainMenuUI.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                        hideLoadingIndicator();
                    }
                } else {
                    hideLoadingIndicator();
                    DialogUtilities.showNoInternetDialog(DriverMainMenuUI.this);
                }
            }
        });

        // Obtén el botón de cerrar sesión
        Button cerrarSesionButton = findViewById(R.id.btn_deslogearse_conductor);

        // Configura el OnClickListener para el botón de cerrar sesión
        cerrarSesionButton.setOnClickListener(v -> DialogUtilities.showLogoutConfirmationDialog(DriverMainMenuUI.this));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            hideLoadingIndicator();
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Verificar si el GPS está habilitado
                if (isGPSEnabled()) {
                    hideLoadingIndicator();
                    // El GPS está apagado, mostrar un diálogo para pedir al usuario que lo active
                    showEnableGPSDialog();
                } else {
                    // El GPS está habilitado, realizar la acción deseada
                    TransitionUI.destino = MapUI.class;
                    Log.d("DEBUG", "FROM: " + DriverMainMenuUI.class.getSimpleName());
                    startActivity(new Intent(DriverMainMenuUI.this, TransitionUI.class));
                }
            } else {
                // Si el usuario deniega los permisos, muestra un mensaje
                Toast.makeText(DriverMainMenuUI.this, "Para ver el mapa, necesitas conceder los permisos de ubicación.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void logout() {
        LogOutHelper.logout(DriverMainMenuUI.this, authServiceImpl);
    }

    // Método para verificar si el GPS está habilitado
    private boolean isGPSEnabled() {
        return getSystemService(Context.LOCATION_SERVICE) == null || !((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // Método para mostrar un diálogo pidiendo al usuario que active el GPS
    private void showEnableGPSDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS Desactivado");
        builder.setMessage("Para continuar, active el GPS en su dispositivo.");
        builder.setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Abre la configuración de ubicación del dispositivo para que el usuario pueda habilitar el GPS
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false); // Evita que el diálogo se cierre al tocar fuera de él
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeDriver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        typeChange = "";
        if (exit) {
            finishAffinity();
        }
    }

    /**
     * Método para mostrar el indicador de carga.
     */
    @Override
    public void showLoadingIndicator() {
        InterfacesUtilities.showLoadingIndicator(DriverMainMenuUI.this, loadingLayoutconductor, loadingIndicatorconductor);
    }

    /**
     * Método para ocultar el indicador de carga.
     */
    @Override
    public void hideLoadingIndicator() {
        InterfacesUtilities.hideLoadingIndicator(DriverMainMenuUI.this, loadingLayoutconductor, loadingIndicatorconductor);
    }

    private void initializeDriver() {
        localDriver = InterfacesUtilities.getLocalDriver(DriverMainMenuUI.this);
        localUser = InterfacesUtilities.getLocalUser(DriverMainMenuUI.this);
        authServiceImpl = new AuthServiceImpl(DriverMainMenuUI.this);
        if (localUser != null) {
            // Obtiene el nombre de usuario y lo muestra en el TextView
            String name = localUser.getFullName();
            TextView textView = findViewById(R.id.txv_conductor_nombre);
            textView.setText(String.format("Driver: %s", name));

        } else {
            Log.d(TAG, "LocalUser is null");
        }
    }

}