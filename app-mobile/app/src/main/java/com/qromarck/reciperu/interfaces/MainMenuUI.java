// MainMenuUI.java
package com.qromarck.reciperu.interfaces;

// Imports
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.DialogUtilities;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.Utilities.LogOutHelper;
import com.qromarck.reciperu.Utilities.LogoutDialogInterface;
import com.qromarck.reciperu.services.AuthServiceImpl;
import com.qromarck.reciperu.services.UserServiceImpl;
import com.google.android.material.navigation.NavigationView;

public class MainMenuUI extends AppCompatActivity implements LogoutDialogInterface {

    // UI Components
    private ImageView imgUser;
    private FrameLayout loadingLayout;
    private ProgressBar loadingIndicator;
    public DrawerLayout drawerLayout;
    private HorizontalScrollView horizontalScrollView;
    private TextView reci;
    private Button mapButton, pointsButton;

    // UserService for managing API interactions
    private UserServiceImpl userServiceImpl;
    private AuthServiceImpl authServiceImpl;

    public static final int INSTALL_UNKNOWN_APPS_PERMISSION_REQUEST_CODE = 1001;
    private static final String TAG = "ECO_RUTA";

    private UserDTO userLogged;
    private String driverId;

    public ImageView getImgUser() {
        return imgUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ui);

        // Initialize components
        initializeUIComponents();

        // Set up listeners
        setUpListeners();

        // Load user information and profile image
        loadUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user information and profile image
        loadUserInfo();
    }

    /**
     * Initialize components.
     */
    private void initializeUIComponents() {
        imgUser = findViewById(R.id.userImageView);
        loadingLayout = findViewById(R.id.loadingLayout);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        drawerLayout = findViewById(R.id.drawer_layout);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        reci = findViewById(R.id.txvReciPoints);
        mapButton = findViewById(R.id.btnVerMapa);
        pointsButton = findViewById(R.id.btnCamara);
        userServiceImpl = new UserServiceImpl(this); // Initialize UserService
        authServiceImpl = new AuthServiceImpl(this);
    }

    /**
     * Set up button listeners.
     */
    private void setUpListeners() {
        // Open navigation drawer
        findViewById(R.id.btn_menu).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Map button listener
        mapButton.setOnClickListener(v -> handleMapButton());

        // Points button listener
        pointsButton.setOnClickListener(v -> handleScanButton());

        // Handle navigation item selection
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this::handleNavigationItemSelected);
        } else {
            Log.e("MainMenuUI", "NavigationView is null");
        }
    }

    /**
     * Load user information and profile image.
     */
    private void loadUserInfo() {
        userLogged = InterfacesUtilities.getLocalUser(MainMenuUI.this);
        if (userLogged != null) {
            //loadProfileImage(userLogged.getId());
            reci.setText(String.valueOf(userLogged.getPoints()));
            TextView txvNombreUsuario = findViewById(R.id.txvUSERNAME);
            txvNombreUsuario.setText(String.format("Bienvenido, %s", userLogged.getFullName()));
        }
        driverId = InterfacesUtilities.getDriverId(MainMenuUI.this);
    }

    /**
     * Handle map button click.
     */
    private void handleMapButton() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        } else {
            startActivity(new Intent(MainMenuUI.this, MapUI.class));
        }
    }

    /**
     * Handle scan points button click.
     */
    private void handleScanButton() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            startBarcodeScanning();
        }
    }

    /**
     * Start barcode scanning.
     */
    private void startBarcodeScanning() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainMenuUI.this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setPrompt("Escanear un QR");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    /**
     * Handle navigation item selection in the drawer.
     *
     * @param item The selected item in the navigation drawer.
     */
    private boolean handleNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_miPerfil) {
            // Handle profile
        } else if (id == R.id.nav_reportar) {
            startActivity(new Intent(MainMenuUI.this, GenerateReportUI.class));
        } else if (id == R.id.nav_rutas) {
            // Handle routes
        } else if (id == R.id.nav_cerrarSesion) {
            // Cierra el menú lateral si es necesario
            if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            DialogUtilities.showLogoutConfirmationDialog(MainMenuUI.this);
        } else if (id == R.id.nav_configuracion) {
            startActivity(new Intent(MainMenuUI.this, SettingsUI.class));
        } else if (id == R.id.nav_mis_reportes) {
            startActivity(new Intent(MainMenuUI.this, ReportsUI.class));
        }

        // Retornar true para indicar que el ítem ha sido manejado
        return true;
    }

    /**
     * Handle user logout process.
     */
    public void logout() {
        LogOutHelper.logout(MainMenuUI.this, authServiceImpl);
    }

    /**
     * Show a loading indicator.
     */
    @Override
    public void showLoadingIndicator() {
        InterfacesUtilities.showLoadingIndicator(this, loadingLayout, loadingIndicator);
    }

    /**
     * Hide the loading indicator.
     */
    @Override
    public void hideLoadingIndicator() {
        InterfacesUtilities.hideLoadingIndicator(this, loadingLayout, loadingIndicator);
    }
}
