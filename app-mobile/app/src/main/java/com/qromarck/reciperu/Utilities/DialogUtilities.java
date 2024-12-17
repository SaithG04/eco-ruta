package com.qromarck.reciperu.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.view.GravityCompat;

import com.qromarck.reciperu.interfaces.DriverMainMenuUI;
import com.qromarck.reciperu.interfaces.InitialLoadingUI;
import com.qromarck.reciperu.interfaces.MainMenuUI;

public class DialogUtilities {

    public static void showNoInternetDialog(Activity activity) {
        if (!activity.isFinishing() && !activity.isDestroyed()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Sin conexión a Internet");
            builder.setMessage("Por favor, conéctate a Internet para continuar.");
            builder.setPositiveButton("Salir", (dialog, which) -> activity.finishAffinity());
            builder.setNegativeButton("Reintentar", (dialog, which) -> {
                if (NetworkUtilities.isNetworkAvailable(activity.getApplicationContext())) {
                    dialog.dismiss();
                    InitialLoadingUI initialLoadingUIActivity = activity instanceof InitialLoadingUI ? (InitialLoadingUI) activity : null;
                    if (initialLoadingUIActivity != null) {
                        initialLoadingUIActivity.load();
                    }
                } else {
                    showNoInternetDialog(activity);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    public static void showNotificationSettingsDialog(Activity activity) {
        if (!activity.isFinishing() && !activity.isDestroyed()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Notificaciones deshabilitadas");
            builder.setMessage("Las notificaciones están deshabilitadas. Habilítelas para recibir notificaciones importantes.");
            builder.setPositiveButton("Ir a configuración", (dialog, which) -> NotificationUtilities.openNotificationSettings(activity));
            builder.setNegativeButton("En otro momento", (dialog, which) -> activity.finishAffinity());
            builder.setCancelable(false);
            builder.show();
        }
    }

    public static void showInstallSettingsDialog(Activity activity) {
        if (!activity.isFinishing() && !activity.isDestroyed()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Permisos");
            builder.setMessage("Necesitamos permisos para actualizar la aplicación.");
            builder.setPositiveButton("Condecer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    @SuppressLint("InlinedApi") Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivityForResult(intent, MainMenuUI.INSTALL_UNKNOWN_APPS_PERMISSION_REQUEST_CODE);
                    activity.finishAffinity();
                }
            });
            builder.setNegativeButton("En otro momento", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finishAffinity();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    public static void showEnableGPSDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("GPS Desactivado");
        builder.setMessage("Para continuar, active el GPS en su dispositivo.");
        builder.setPositiveButton("Configuración", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activity.startActivity(intent);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
    }

    public static void showLogoutConfirmationDialog(LogoutDialogInterface uiInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) uiInterface);
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?");

        builder.setPositiveButton("Sí", (dialog, which) -> {
            // Cerrar el drawer si es necesario (solo para MainMenuUI)
            if (uiInterface instanceof MainMenuUI) {
                ((MainMenuUI) uiInterface).drawerLayout.closeDrawer(GravityCompat.START);
            }

            uiInterface.showLoadingIndicator();

            if (NetworkUtilities.isNetworkAvailable(uiInterface.getApplicationContext())) {
                if (uiInterface instanceof MainMenuUI) {
                    uiInterface.logout();
                } else if (uiInterface instanceof DriverMainMenuUI) {
                    uiInterface.logout();
                }
            } else {
                uiInterface.hideLoadingIndicator();
                DialogUtilities.showNoInternetDialog((Activity) uiInterface);
            }
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
