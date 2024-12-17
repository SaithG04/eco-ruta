package com.qromarck.reciperu.interfaces;

import static com.qromarck.reciperu.exception.GeneralErrors.handleLoginFailure;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.qromarck.reciperu.DTO.DriverDTO;
import com.qromarck.reciperu.DTO.DriverMapper;
import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.DTO.UserMapper;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.DialogUtilities;
import com.qromarck.reciperu.Utilities.ErrorUtilities;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.Utilities.NetworkUtilities;
import com.qromarck.reciperu.services.AuthServiceImpl;
import com.qromarck.reciperu.services.DriverService;
import com.qromarck.reciperu.services.DriverServiceImpl;
import com.qromarck.reciperu.services.UserService;
import com.qromarck.reciperu.services.UserServiceImpl;

import java.util.Objects;

public class LoginUI extends AppCompatActivity {

    // Declaración de variable
    private EditText edtCorreo;
    private EditText edtContrasena;
    private FrameLayout loadingLayout;
    private ProgressBar loadingIndicator;
    private FirebaseAuth mAuth;
    private AuthServiceImpl authServiceImpl;
    private DriverService driverService;
    private UserService userService;

    private static final int RC_SIGN_IN = 1000;
    private static final String TAG = "ECO_RUTA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configurar el diseño y bordes transparentes
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_ui);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar elementos de la interfaz de usuario
        edtCorreo = findViewById(R.id.edtCorreoLOGIN);
        edtContrasena = findViewById(R.id.edtContraLOGIN);
        loadingLayout = findViewById(R.id.loadingLayout);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        Button btnRegistrarse = findViewById(R.id.btnRegistrar);
        Button btnLogin = findViewById(R.id.btnLoginLOG);
        TextView restablecer = findViewById(R.id.txvRestablecer);
        TextView reenviar = findViewById(R.id.txvReenviar);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Init
        authServiceImpl = new AuthServiceImpl(this);
        driverService = new DriverServiceImpl(this);
        userService = new UserServiceImpl(this);

        // Configurar el botón para abrir la actividad de registro de usuario
        btnRegistrarse.setOnClickListener(view -> {
            //TransitionUI.destino = RegistroUsuarioUI.class;
            TransitionUI.destino = DNIValidationUI.class;
            Log.d("DEBUG", "FROM: " + LoginUI.class.getSimpleName());
            startActivity(new Intent(LoginUI.this, TransitionUI.class));
        });

        // Configurar el botón de inicio de sesión
        btnLogin.setOnClickListener(view -> {
            InterfacesUtilities.hideKeyboard(LoginUI.this);
            String correo = edtCorreo.getText().toString();
            String password = edtContrasena.getText().toString();
            if (correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginUI.this, "¡Ey, faltan datos!", Toast.LENGTH_SHORT).show();
            } else {
                if (NetworkUtilities.isNetworkAvailable(getApplicationContext())) {
                    loginWithEmail(correo, password);
                } else {
                    hideLoadingIndicator();
                    DialogUtilities.showNoInternetDialog(LoginUI.this);
                }
            }
        });

        // Configurar el texto para restablecer la contraseña
        restablecer.setOnClickListener(v -> {
            /*TransitionUI.destino = ResetPasswordUI.class;
            startActivity(new Intent(LoginUI.this, TransitionUI.class));*/
            InterfacesUtilities.showMessage(LoginUI.this, "Restablecer", "Muy pronto...");
        });

        // Configurar el texto para reenviar el correo de verificación
        reenviar.setOnClickListener(view -> {
            /*showLoadingIndicator();
            String correo = edtCorreo.getText().toString();
            String password = edtContrasena.getText().toString();
            if (correo.isEmpty() || password.isEmpty()) {
                hideLoadingIndicator();
                Toast.makeText(LoginUI.this, "Debes ingresar tu correo y contraseña.", Toast.LENGTH_SHORT).show();
            } else {
                if (NetworkUtilities.isNetworkAvailable(getApplicationContext())) {
                    //verificarYReenviarCorreo(correo, password);
                } else {
                    hideLoadingIndicator();
                    DialogUtilities.showNoInternetDialog(LoginUI.this);
                }
            }*/
            InterfacesUtilities.showMessage(LoginUI.this, "Reenviar", "Muy pronto...");
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        edtCorreo.setText("");
        edtContrasena.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    /*
     * Método para reenviar el correo de verificación.

    private void verificarYReenviarCorreo(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        hideLoadingIndicator();
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Correo de verificación reenviado a " + email, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.e("ReenviarVerificacion", "Error al reenviar correo de verificación", task1.getException());
                                            Toast.makeText(getApplicationContext(), "Error al reenviar correo de verificación.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        hideLoadingIndicator();
                        Log.e("ReenviarVerificacion", "Error al reautenticar", task.getException());
                        Toast.makeText(getApplicationContext(), "Verifica tus credenciales.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    */

    public void loginWithEmail(String correo, String password) {
        showLoadingIndicator();

        // Crear un usuario con el correo proporcionado
        UserDTO userSearched = new UserDTO();
        userSearched.setEmail(correo);
        UserService userService = new UserServiceImpl(this);

        // Buscar usuario en la base de datos
        userService.getUserByEmail(userSearched.getEmail(), apiResponse -> {
            if(apiResponse.getCode() == 200) {
                // Usar el DTO Mapper
                Log.d("DEBUG_JSON", "jsonResponse: " + apiResponse.getData().toString());
                UserDTO userDTO = UserMapper.mapToDTO(apiResponse.getData(), UserDTO.class);
                logIn(userDTO, password);
            }else {
                InterfacesUtilities.showError(LoginUI.this, TAG, "Error", apiResponse.getMessage());
            }
        }, e -> {
            ErrorUtilities.validateExceptionType(LoginUI.this, e);
            hideLoadingIndicator();
        });
    }


    private void logIn(UserDTO userDTO, String password) {

        FirebaseMessaging.getInstance().subscribeToTopic("horarios")
                .addOnCompleteListener(subTopicTask -> {
                    if (!subTopicTask.isSuccessful()) {
                        hideLoadingIndicator();
                        mAuth.signOut();

                        Log.w("ERROR", "Suscripción fallida", subTopicTask.getException());
                        InterfacesUtilities.showDefaultError(getApplicationContext(), TAG, Objects.requireNonNull(subTopicTask.getException()).getMessage());
                    } else {
                        mAuth.signInWithEmailAndPassword(userDTO.getEmail(), password).addOnCompleteListener(authTask -> {
                            if (authTask.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                assert user != null;
                                user.getIdToken(true)
                                        .addOnCompleteListener(getIdTask -> {
                                            if (getIdTask.isSuccessful()) {
                                                String idToken = getIdTask.getResult().getToken();
                                                Log.d("DEBUG", "ID TOKEN: " + idToken);
                                                authServiceImpl.logIn(idToken, apiResponse -> {
                                                    Object data = apiResponse.getData();
                                                    if (userDTO.getEmail().endsWith("@municipalidad.pe")){
                                                        continueToDriverMainMenu(data);
                                                    }else {
                                                        continueToMainMenu(data);
                                                    }
                                                }, e -> {
                                                    hideLoadingIndicator();
                                                    ErrorUtilities.validateExceptionType(LoginUI.this, e);
                                                });
                                            } else {
                                                // Manejar error si no se puede obtener el token
                                                Log.e("Login", "Error getting ID token");
                                                hideLoadingIndicator();
                                            }
                                        });
                            }
                        }).addOnFailureListener(e -> {
                            handleLoginFailure(getApplicationContext(), edtCorreo, edtContrasena, e);
                            hideLoadingIndicator();
                        });
                    }
                });
    }

    private void continueToDriverMainMenu(Object data) {
        DriverDTO driverDTO = DriverMapper.mapToDTO(data, DriverDTO.class);
        InterfacesUtilities.saveLocalDriver(LoginUI.this, driverDTO);

        userService.getUserByUid(driverDTO.getUserId(), apiResponse -> {
            hideLoadingIndicator();
            UserDTO dto = UserMapper.mapToDTO(apiResponse.getData(), UserDTO.class);
            InterfacesUtilities.saveLocalUser(LoginUI.this, dto);
            TransitionUI.destino = DriverMainMenuUI.class;
            startActivity(new Intent(LoginUI.this, TransitionUI.class));
            finish();
            InterfacesUtilities.showMessage(getApplicationContext(), TAG, "Welcome driver.");
        }, e -> {
            hideLoadingIndicator();
            ErrorUtilities.validateExceptionType(LoginUI.this, e);
        });
    }

    private void continueToMainMenu(Object data) {
        UserDTO dto = UserMapper.mapToDTO(data, UserDTO.class);
        InterfacesUtilities.saveLocalUser(LoginUI.this, dto);

        driverService.getDriverByStatusAndLocationId("WORKING", dto.getLocationId(), apiResponse -> {
            DriverDTO driverDTO = DriverMapper.mapToDTO(apiResponse.getData(), DriverDTO.class);
            InterfacesUtilities.saveDriverId(LoginUI.this, driverDTO.getUserId());
        }, e ->{
            ErrorUtilities.validateExceptionType(LoginUI.this, e);
        });

        TransitionUI.destino = MainMenuUI.class;
        startActivity(new Intent(LoginUI.this, TransitionUI.class));
        finish();
        InterfacesUtilities.showMessage(getApplicationContext(), TAG, "Welcome.");
        hideLoadingIndicator();
    }

    private void showLoadingIndicator() {
        InterfacesUtilities.showLoadingIndicator(LoginUI.this, loadingLayout, loadingIndicator);
    }

    private void hideLoadingIndicator() {
        InterfacesUtilities.hideLoadingIndicator(LoginUI.this, loadingLayout, loadingIndicator);
    }
}
