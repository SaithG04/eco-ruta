// DNIValidationUI.java
package com.qromarck.reciperu.interfaces;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qromarck.reciperu.DTO.ReniecDniMapper;
import com.qromarck.reciperu.DTO.ReniecDniDTO;
import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.Utilities.NetworkUtilities;
import com.qromarck.reciperu.exception.ApiResponseException;
import com.qromarck.reciperu.services.UserServiceImpl;

import java.util.Objects;

public class DNIValidationUI extends AppCompatActivity {

    private EditText txtDNI;
    private Button btnValidate;
    private FrameLayout loadingLayout;
    private ProgressBar loadingIndicator;

    private static final String TAG = "API_ECO_RUTA";

    private UserServiceImpl userServiceImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_dni_ui);

        userServiceImpl = new UserServiceImpl(this); // Instancia de UserService

        initializeUI();
    }

    private void initializeUI() {
        txtDNI = findViewById(R.id.txtDNI);
        btnValidate = findViewById(R.id.btnValidar);
        loadingLayout = findViewById(R.id.loadingLayout);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        btnValidate.setOnClickListener(v -> {
            String dni = txtDNI.getText().toString().trim();
            if (InterfacesUtilities.isDniValid(dni)) {
                consultReniecService(dni);
            } else {
                InterfacesUtilities.showError(this, TAG, "DNI inválido", "El DNI debe tener 8 dígitos.");
            }
        });
    }

    private void consultReniecService(String dni) {
        showLoadingIndicator();

        if (NetworkUtilities.isNetworkAvailable(getApplicationContext())) {

            userServiceImpl.validateDNI(dni, apiResponse -> {
                if (apiResponse.getCode() == 200) {


                    ReniecDniDTO dniDTO = ReniecDniMapper.mapToDTO(apiResponse.getData(), ReniecDniDTO.class);
                    Log.d(TAG, "RENIEC Info: " + dniDTO.toString());

                    if (dniDTO.getNombres() == null || dniDTO.getNombres().isEmpty()) {
                        InterfacesUtilities.showError(DNIValidationUI.this, TAG, "Nombres no encontrados", "DNI inválido");
                        hideLoadingIndicator();
                        return;
                    }

                    UserDTO user = getUserDTO(dniDTO);
                    continueRegistration(user);
                    hideLoadingIndicator();
                } else {
                    // Mostrar error si el código no es 200
                    hideLoadingIndicator();
                    InterfacesUtilities.showError(DNIValidationUI.this, TAG, "Server Error: " + apiResponse.getMessage(), apiResponse.getMessage());
                }
            }, e -> {
                hideLoadingIndicator();
                if (e instanceof ApiResponseException && Objects.requireNonNull(e.getMessage()).contains("422 Unprocessable Entity")) {
                    InterfacesUtilities.showError(DNIValidationUI.this, TAG, "DNI inválido", "DNI inválido");
                } else if (e instanceof ApiResponseException) {
                    InterfacesUtilities.showError(DNIValidationUI.this, "API_EXCEPTION", "Server Error: " + e.getMessage(), e.getMessage());
                } else {
                    InterfacesUtilities.showDefaultError(DNIValidationUI.this, "API_EXCEPTION", "Request failed: " + e.getMessage());
                }
            });
        } else {
            hideLoadingIndicator();
            // Mostrar mensaje de error por falta de conexión
            InterfacesUtilities.showError(DNIValidationUI.this, "NO_INTERNET", "No internet connection", "Sin conexión a Internet");
        }
    }

    private @NonNull UserDTO getUserDTO(ReniecDniDTO dniDTO) {
        String nombres = dniDTO.getNombres();
        String apellidoPaterno = dniDTO.getApellidoPaterno();
        String apellidoMaterno = dniDTO.getApellidoMaterno();
        String numeroDocumento = dniDTO.getNumeroDocumento();

        // Concatenar los nombres y apellidos para formar el nombre completo
        String fullName = nombres + " " + apellidoPaterno + " " + apellidoMaterno;

        return new UserDTO(fullName, numeroDocumento);
    }

    private void continueRegistration(UserDTO userDTO) {
        InterfacesUtilities.saveLocalUser(getApplicationContext(), userDTO);
        TransitionUI.destino = RegisterUserUI.class;
        startActivity(new Intent(DNIValidationUI.this, TransitionUI.class));
        finish();
    }

    private void showLoadingIndicator() {
        loadingLayout.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        loadingLayout.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.GONE);
    }
}
