package com.qromarck.reciperu.interfaces;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.DTO.UserMapper;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.ErrorUtilities;
import com.qromarck.reciperu.Utilities.DeviceUtils;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.Utilities.NetworkUtilities;
import com.qromarck.reciperu.services.UserService;
import com.qromarck.reciperu.services.UserServiceImpl;

public class RegisterUserUI extends AppCompatActivity {

    // UI Components
    private EditText edtEmail, edtPassword, edtConfirm;
    private FrameLayout loadingLayout;
    private ProgressBar loadingIndicator;
    private TextView tvFullName;

    // Firebase Authentication and Google Sign-In components
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1000;
    private static final String TAG = "GoogleSignIn";

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_ui);

        // Initialize Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        edtEmail = findViewById(R.id.edtCorreoREG);
        edtPassword = findViewById(R.id.edtContrasenaREG);
        edtConfirm = findViewById(R.id.edtContrasenaConfirmREG);
        Button registrarEmail = findViewById(R.id.btnRegCorreo);
        Button registrarGoogle = findViewById(R.id.btnRegGoogle);
        loadingLayout = findViewById(R.id.loadingLayout);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        tvFullName = findViewById(R.id.txtNombreCompleto);

        initUserData();

        // Set up listeners for buttons
        registrarEmail.setOnClickListener(v -> {
            if (validateCampos()) {
                UserDTO newUser = createUserDTO();
                String password = edtPassword.getText().toString();
                registerWithEmail(newUser, password);
            }
        });

        registrarGoogle.setOnClickListener(view -> {
            InterfacesUtilities.showMessage(RegisterUserUI.this, "Google", "Muy pronto...");
        });
    }

    private void initUserData() {
        UserDTO userRetrieved = InterfacesUtilities.getLocalUser(getApplicationContext());
        tvFullName.setText(userRetrieved.getFullName());
    }

    private void registerWithEmail(UserDTO userForRegister, String password) {
        showLoadingIndicator();

        if (NetworkUtilities.isNetworkAvailable(getApplicationContext())) {

            userService = new UserServiceImpl(getApplicationContext());
            userService.create(userForRegister, password, response -> {

                if (response.getCode() == 200) {

                    UserDTO registeredUser = UserMapper.mapToDTO(response.getData(), UserDTO.class);
                    // Log del usuario registrado
                    Log.d(TAG, "User Info: " + registeredUser.toString());
                    TransitionUI.destino = LoginUI.class;

                    // Redirigir al usuario a la pantalla de login
                    startActivity(new Intent(RegisterUserUI.this, TransitionUI.class));
                    finish();
                    InterfacesUtilities.showMessage(getApplicationContext(), TAG, response.getMessage());
                } else {
                    // Mostrar error si el código no es 200
                    InterfacesUtilities.showError(RegisterUserUI.this, TAG, "Server Error: " + response.getMessage(), response.getMessage());
                }
                hideLoadingIndicator();
            }, e -> {
                hideLoadingIndicator();
                ErrorUtilities.validateExceptionType(RegisterUserUI.this, e);
            });
        } else {
            hideLoadingIndicator();
            // Mostrar mensaje de error por falta de conexión
            InterfacesUtilities.showError(RegisterUserUI.this, "NO_INTERNET", "No internet connection", "Sin conexión a Internet");
        }
    }

    private boolean validateCampos() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirm.getText().toString();

        if (email.isEmpty()) {
            edtEmail.setError("Ingrese su correo");
            return false;
        } else if (password.isEmpty()) {
            edtPassword.setError("Ingrese una clave");
            return false;
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private UserDTO createUserDTO() {
        UserDTO user = InterfacesUtilities.getLocalUser(getApplicationContext());
        user.setEmail(edtEmail.getText().toString());
        user.setIdDevice(DeviceUtils.getAndroidID(getApplicationContext()));
        user.setLocationId(1L);
        return user;
    }

    private void signInWithGoogleAccount() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                hideLoadingIndicator();
                Log.e(TAG, "Google sign in failed", e);
            }
        }
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
