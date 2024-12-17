package com.qromarck.reciperu.exception;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.qromarck.reciperu.Utilities.InterfacesUtilities;

import java.util.Objects;

public class GeneralErrors {

    public static void handleLoginFailure(Context context, EditText edtCorreo, EditText edtContrasena, Exception e) {
        String errorMessage;
        //int duration = Toast.LENGTH_SHORT;

        if (Objects.requireNonNull(e.getMessage()).contains("The email address is badly formatted.")) {
            errorMessage = "¡Ups! Parece que el email que has ingresado no es válido.";
            edtCorreo.requestFocus();
        } else if (e.getMessage().contains("The supplied auth credential is incorrect, malformed or has expired.") || e.getMessage().contains("The password is invalid or the user does not have a password.")) {
            errorMessage = "Verifique nuevamente su contraseña por favor.";
            edtContrasena.setText("");
            edtContrasena.requestFocus();
        } else if (e.getMessage().contains("We have blocked all requests from this device due to unusual activity. Try again later. [ Access to this account has been temporarily disabled due to many failed login attempts. You can immediately restore it by resetting your password or you can try again later. ]")) {
            errorMessage = "Se ha bloqueado temporalmente el acceso a tu cuenta debido a demasiados intentos de inicio de sesión fallidos. Por favor, restablece tu contraseña o inténtalo más tarde.";
            //duration = Toast.LENGTH_LONG;
        } else if (e.getMessage().contains("A network error (such as timeout, interrupted connection or unreachable host) has occurred.")) {
            errorMessage = "Verifique su conexión a internet.";
            edtCorreo.setText("");
            edtContrasena.setText("");
            edtCorreo.requestFocus();
        } else {
            errorMessage = "Lo sentimos, ha ocurrido un error.";
        }
        InterfacesUtilities.showError(context, "LOGIN", e.getMessage(), errorMessage);
    }
}
