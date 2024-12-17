package com.qromarck.reciperu.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.qromarck.reciperu.interfaces.DriverMainMenuUI;
import com.qromarck.reciperu.interfaces.LoginUI;
import com.qromarck.reciperu.interfaces.MainMenuUI;
import com.qromarck.reciperu.interfaces.TransitionUI;
import com.qromarck.reciperu.services.AuthServiceImpl;

public class LogOutHelper {

    public static void logout(Activity activity, AuthServiceImpl authServiceImpl) {
        Context context = activity.getApplicationContext();
        boolean isMainMenu = activity instanceof MainMenuUI;
        boolean isDriverMainMenu = activity instanceof DriverMainMenuUI;

        // Mostrar indicador de carga según el tipo de actividad
        showLoadingIndicatorToLogOut(activity, isMainMenu, isDriverMainMenu);

        // Obtener el UID del usuario local
        String uid = InterfacesUtilities.getLocalUser(context).getUid();

        // Llamar al método de logout
        authServiceImpl.logOut(uid, apiResponse -> {
            if (apiResponse.getCode() == 200) {
                // Mostrar mensaje de éxito
                InterfacesUtilities.showMessage(context, "TAG", apiResponse.getMessage());

                // Limpiar las preferencias de usuario
                InterfacesUtilities.saveLocalUser(context, null);
                InterfacesUtilities.saveDriverId(context, null);
                InterfacesUtilities.saveLocalDriver(context, null);

                // Redirigir a la pantalla de login
                redirectToLogin(activity, context);
            } else if (apiResponse.getCode() == 500){
                InterfacesUtilities.showDefaultError(context, "LOGOUT", "Server Error: " + apiResponse.getMessage());
            } else {
                InterfacesUtilities.showError(context, "LOGOUT", "Server Error: " + apiResponse.getMessage(), apiResponse.getMessage());
            }
            // Ocultar indicador de carga
            hideLoadingIndicatorToLogOut(activity, isMainMenu, isDriverMainMenu);
        }, e -> {
            // Manejar error de logout
            hideLoadingIndicatorToLogOut(activity, isMainMenu, isDriverMainMenu);
            ErrorUtilities.validateExceptionType(activity, e);
        });
    }

    private static void showLoadingIndicatorToLogOut(Activity activity, boolean isMainMenu, boolean isDriverMainMenu) {
        if (isMainMenu) {
            ((MainMenuUI) activity).showLoadingIndicator();
        } else if (isDriverMainMenu) {
            ((DriverMainMenuUI) activity).showLoadingIndicator();
        }
    }

    private static void hideLoadingIndicatorToLogOut(Activity activity, boolean isMainMenu, boolean isDriverMainMenu) {
        if (isMainMenu) {
            ((MainMenuUI) activity).hideLoadingIndicator();
        } else if (isDriverMainMenu) {
            ((DriverMainMenuUI) activity).hideLoadingIndicator();
        }
    }

    private static void redirectToLogin(Activity activity, Context context) {
        TransitionUI.destino = LoginUI.class;
        activity.startActivity(new Intent(context, TransitionUI.class));
        activity.finish();
    }

}
