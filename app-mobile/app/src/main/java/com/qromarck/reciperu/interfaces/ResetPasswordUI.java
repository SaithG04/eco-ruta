package com.qromarck.reciperu.interfaces;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;

public class ResetPasswordUI extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FrameLayout loadingLayout;
    private ProgressBar loadingIndicator;
    private UserDTO userLoggedOnSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restablecer_contra);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userLoggedOnSystem = InterfacesUtilities.getLocalUser(ResetPasswordUI.this);

        EditText gmailEditText = findViewById(R.id.edtGmailUser);
        Button enviar = findViewById(R.id.btnEnviarGmailUser);

        if(userLoggedOnSystem != null){
            String email = userLoggedOnSystem.getEmail();
            gmailEditText.setText(email);
            gmailEditText.setKeyListener(null);
            gmailEditText.setFocusable(false);
        }

        mAuth = FirebaseAuth.getInstance();
        loadingLayout = findViewById(R.id.loadingLayout);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingIndicator();
                InterfacesUtilities.hideKeyboard(ResetPasswordUI.this);
                // Obtiene el texto ingresado en el EditText
                String email = gmailEditText.getText().toString().trim();

                // Verifica si el campo de correo electrónico no está vacío
                if (email.isEmpty()) {
                    Toast.makeText(ResetPasswordUI.this, "Ingrese su correo, Por favor.", Toast.LENGTH_SHORT).show();
                    hideLoadingIndicator();
                    return;
                }

                /*
                // Crear un usuario con el correo proporcionado
                UserDTO userSearched = new UserDTO();
                userSearched.setEmail(email);
                UserDAO userDAO = new UserDAOImpl();
                userDAO.setEntity(userSearched);

                // Buscar usuario en Firebase
                userDAO.getUserByEmail(userSearched.getEmail(), new OnSuccessListener<UserDTO>() {
                    @Override
                    public void onSuccess(UserDTO users) {
                        if (users == null) {
                            hideLoadingIndicator(); // Ocultar indicador de carga
                            Toast.makeText(getApplicationContext(), "No hay ninguna cuenta asociada a este correo.", Toast.LENGTH_LONG).show();
                        } else {
                            /*if (InterfacesUtilities.isGoogleEmail(email)){
                                hideLoadingIndicator();
                                Toast.makeText(ResetPasswordUI.this, "Su correo pertenece a Google, contáctese con su proveedor.", Toast.LENGTH_LONG).show();
                            }else{*//*
                                // Envía el correo de restablecimiento de contraseña
                                mAuth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Correo de restablecimiento enviado con éxito
                                                    Toast.makeText(ResetPasswordUI.this, "Se ha enviado un correo para restablecer su contraseña", Toast.LENGTH_SHORT).show();
                                                    hideLoadingIndicator();
                                                    finish(); // Cierra la actividad actual
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Error al enviar el correo de restablecimiento
                                                hideLoadingIndicator();
                                                Toast.makeText(ResetPasswordUI.this, "Error al enviar el correo de restablecimiento. ", Toast.LENGTH_SHORT).show();
                                                e.printStackTrace(System.out);
                                            }
                                        });
                            //}
                        }
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace(System.out);
                    }
                });*/

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideLoadingIndicator();
        TransitionUI.destino = userLoggedOnSystem != null ? MainMenuUI.class : LoginUI.class;
        Log.d("DEBUG", "FROM: " + RegisterUserUI.class.getSimpleName());
        startActivity(new Intent(ResetPasswordUI.this, TransitionUI.class));
        finish();
    }

    /**
     * Método para mostrar el indicador de carga.
     */
    private void showLoadingIndicator() {
        InterfacesUtilities.showLoadingIndicator(ResetPasswordUI.this, loadingLayout, loadingIndicator);
    }

    /**
     * Método para ocultar el indicador de carga.
     */
    public void hideLoadingIndicator() {
        InterfacesUtilities.hideLoadingIndicator(ResetPasswordUI.this, loadingLayout, loadingIndicator);
    }
}