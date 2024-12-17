package com.qromarck.reciperu.interfaces;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.qromarck.reciperu.DTO.DriverDTO;
import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.Utilities.DialogUtilities;
import com.qromarck.reciperu.Utilities.NetworkUtilities;
import com.qromarck.reciperu.Utilities.NotificationUtilities;

public class InitialLoadingUI extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 2000; // 2 segundos
    FrameLayout loadingLayout;
    ProgressBar progressBar;
    TextView progressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transition_ui);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadingLayout = findViewById(R.id.loadingLayout);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TOKEN", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    //String msg = getString(R.string.msg_token_fmt, token);
                    Log.d("TOKEN", "El token es: " + token);
                    //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                });


        load();
    }

    public void load() {
        new Handler().postDelayed(() -> {
            if (NetworkUtilities.isNetworkAvailable(getApplicationContext())) {

                //UpdateUtilities.checkForUpdate(InitialLoadingUI.this, progressBar, progressText, loadingLayout);

                if (!NotificationUtilities.areNotificationsEnabled(getApplicationContext())) {
                    DialogUtilities.showNotificationSettingsDialog(InitialLoadingUI.this);
                } else {
                    UserDTO user = InterfacesUtilities.getLocalUser(getApplicationContext());
                    DriverDTO driver = InterfacesUtilities.getLocalDriver(getApplicationContext());

                    if (user != null && driver != null && user.getStatus() != null) {
                        Log.d("InitialLoadingUI", "El conductor está logueado");
                        initActivity(DriverMainMenuUI.class); // Conductor logueado, ir al menú
                    } else if (user != null && user.getStatus() != null) {
                        Log.d("InitialLoadingUI", "El usuario está logueado");
                        initActivity(MainMenuUI.class); // Usuario logueado, ir al menú
                    } else {
                        Log.d("InitialLoadingUI", "El usuario no está logueado");
                        initActivity(LoginUI.class); // Usuario no logueado, ir a login
                    }
                }
            } else {
                DialogUtilities.showNoInternetDialog(InitialLoadingUI.this);
            }

        }, SPLASH_SCREEN_TIMEOUT);
    }

    private void initActivity(Class<?> destinationActivity) {
        Intent intent = new Intent(InitialLoadingUI.this, destinationActivity);
        startActivity(intent);
        finish();
    }

}