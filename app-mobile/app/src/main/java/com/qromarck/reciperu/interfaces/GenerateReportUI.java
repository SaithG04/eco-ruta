package com.qromarck.reciperu.interfaces;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.qromarck.reciperu.DTO.ReportDTO;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.ErrorUtilities;
import com.qromarck.reciperu.Utilities.FileStorageFirbaseHelper;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.services.ReportService;
import com.qromarck.reciperu.services.ReportServiceImpl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class GenerateReportUI extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    //private static final int STORAGE_PERMISSION_CODE = 101;

    private ImageView reportImageView;
    private EditText descriptionEditText;
    private Uri photoUri;
    private FrameLayout loadingLayout;
    private ProgressBar loadingIndicator;

    private ReportService reportService;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityResultLauncher<String> permissionRequestLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_ui);

        // Inicialización de vistas
        reportImageView = findViewById(R.id.imgviewReporte);
        descriptionEditText = findViewById(R.id.edtDescripcionReporte);
        loadingLayout = findViewById(R.id.loadingLayout);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        reportService = new ReportServiceImpl(this);
        // Inicializar el cliente de ubicación
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Inicializar el ActivityResultLauncher para permisos
        permissionRequestLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Log.d("ECO_RUTA", "Permiso concedido");
                        // Si el permiso fue concedido, puedes proceder
                        dispatchTakePictureIntent();
                    } else {
                        Log.d("ECO_RUTA", "Permiso denegado");
                    }
                });

        // Botón para capturar la foto
        Button captureButton = findViewById(R.id.btntomarFotoreporte);
        captureButton.setOnClickListener(v -> openCamera());

        // Botón para subir la foto a Firebase
        Button uploadButton = findViewById(R.id.btnEnviarReporte);
        uploadButton.setOnClickListener(v -> {
            if (photoUri != null) {
                uploadImageToFirebase(photoUri);  // Llamamos al helper para subir la imagen
            } else {
                Toast.makeText(this, "Por favor, capture una foto primero.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {


            reportImageView.setImageURI(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de cámara concedido, ahora se puede abrir la cámara
                Log.d("ECO_RUTA", "Permiso de cámara concedido");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    openCamera(); // Vuelve a intentar abrir la cámara si el permiso de cámara fue concedido
                }
            } else {
                Log.d("ECO_RUTA", "Permiso de cámara denegado");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void openCamera() {
        Log.d("ECO_RUTA", "Abriendo la cámara...");

        // Primero, verifica si se tienen los permisos necesarios
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionRequestLauncher.launch(Manifest.permission.CAMERA);
            return;
        }
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        // Crear un archivo temporal para la foto
        File photoFile = createImageFile();
        if (photoFile != null) {
            Log.d("ECO_RUTA", "Ruta del archivo temporal: " + photoFile.getAbsolutePath());
            photoUri = FileProvider.getUriForFile(this, "com.qromarck.reciperu.fileprovider", photoFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        } else {
            Log.e("ECO_RUTA", "Error al crear el archivo temporal");
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (Exception e) {
            Log.e("ECO_RUTA", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    private void uploadImageToFirebase(Uri photoUri) {
        showLoadingIndicator();

        FileStorageFirbaseHelper.uploadImageToFirebase(photoUri, photoUrl -> {
            Log.d("REPORT", "Imagen subida correctamente. URL: " + photoUrl);
            getCurrentLocationAndCreateReport(photoUrl);
        }, e -> {
            hideLoadingIndicator();
            InterfacesUtilities.showDefaultError(GenerateReportUI.this, "REPORT", e.getMessage());
        });
    }

    private void getCurrentLocationAndCreateReport(String photoUrl) {

        // Verificar permisos de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 102);
            return;
        }

        // Obtener la ubicación
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                // Crear el ReportDTO
                String description = descriptionEditText.getText().toString();
                if (description.isEmpty()) {
                    description = "Sin descripción";
                }

                ReportDTO reportDTO = new ReportDTO();
                reportDTO.setDescription(description);
                reportDTO.setLatitude(latitude);
                reportDTO.setLongitude(longitude);
                reportDTO.setReportDate(null);
                reportDTO.setStatus("PENDING");
                reportDTO.setUserId(InterfacesUtilities.getLocalUser(GenerateReportUI.this).getUid()); // ID de usuario que hace el reporte
                reportDTO.setPhotoUrl(photoUrl); // URL de la imagen subida

                // Llamar al servicio para crear el reporte
                reportService.create(reportDTO, apiResponse -> {
                    hideLoadingIndicator();
                    InterfacesUtilities.showMessage(GenerateReportUI.this, "REPORT", "Reporte creado correctamente");
                    reportImageView.setImageBitmap(null);
                    descriptionEditText.setText(null);
                }, e -> {
                    hideLoadingIndicator();
                    ErrorUtilities.validateExceptionType(GenerateReportUI.this, e);
                });
            } else {
                hideLoadingIndicator();
                InterfacesUtilities.showError(GenerateReportUI.this, "REPORT", "Location is null", "Unable to get location");
            }
        });
    }

    private void showLoadingIndicator() {
        InterfacesUtilities.showLoadingIndicator(GenerateReportUI.this, loadingLayout, loadingIndicator);
    }

    private void hideLoadingIndicator() {
        InterfacesUtilities.hideLoadingIndicator(GenerateReportUI.this, loadingLayout, loadingIndicator);
    }
}
