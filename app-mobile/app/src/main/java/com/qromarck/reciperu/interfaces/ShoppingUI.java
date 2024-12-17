package com.qromarck.reciperu.interfaces;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.R;
import com.qromarck.reciperu.Utilities.DialogUtilities;
import com.qromarck.reciperu.Utilities.InterfacesUtilities;
import com.qromarck.reciperu.Utilities.NetworkUtilities;
import com.qromarck.reciperu.Utilities.SendEmail;

import javax.mail.MessagingException;


public class ShoppingUI extends AppCompatActivity {

    private TextView ptos;

    public static String typeChange = "";

    private FrameLayout loadingLayout;
    private ProgressBar loadingIndicator;

    public TextView getPtos() {
        return ptos;
    }

    int precio = 0;

    Button btn1, btn2, btn3;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reci_shop);

        //Recuperamos usuario logeado
        UserDTO userLoggedOnSystem = InterfacesUtilities.getLocalUser(ShoppingUI.this);

        //Otener puntos de usuario logeado en sistema
        Long recipoints = userLoggedOnSystem.getPoints();
        ptos = findViewById(R.id.txvEcoPoints);
        //Colocar ReciPoints:
        ptos.setText(String.valueOf(recipoints));

        //PUNTOS
        //BOTONES
        btn1 = findViewById(R.id.btnProd1);
        btn2 = findViewById(R.id.btnProd2);
        btn3 = findViewById(R.id.btnProd3);

        loadingLayout = findViewById(R.id.loadingLayout);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (NetworkUtilities.isNetworkAvailable(getApplicationContext())) {
                    //Recuperamos puntos usuario logeado
                    UserDTO userLoggedOnSystem = InterfacesUtilities.getLocalUser(ShoppingUI.this);
                    //Otener puntos de usuario logeado en sistema
                    Long recipointsstatus = userLoggedOnSystem.getPoints();
                    precio = 5000;
                    if (recipointsstatus >= precio) {
                        RestarPtos(precio);
                        try {
                            enviarEmail();
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(ShoppingUI.this, "NO CUENTA CON ECOPOINTS SUFICIENTES!!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                   hideLoadingIndicator();
                    DialogUtilities.showNoInternetDialog(ShoppingUI.this);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtilities.isNetworkAvailable(getApplicationContext())) {
                    //Recuperamos puntos usuario logeado
                    UserDTO userLoggedOnSystem = InterfacesUtilities.getLocalUser(ShoppingUI.this);
                    //Otener puntos de usuario logeado en sistema
                    Long recipointsstatus = userLoggedOnSystem.getPoints();
                    precio = 7000;
                    if (recipointsstatus >= precio) {
                        RestarPtos(precio);
                        try {
                            enviarEmail();
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(ShoppingUI.this, "NO CUENTA CON ECOPOINTS SUFICIENTES!!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                   hideLoadingIndicator();
                    DialogUtilities.showNoInternetDialog(ShoppingUI.this);
                }
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtilities.isNetworkAvailable(getApplicationContext())) {
                    //Recuperamos puntos usuario logeado
                    UserDTO userLoggedOnSystem = InterfacesUtilities.getLocalUser(ShoppingUI.this);
                    //Otener puntos de usuario logeado en sistema
                    Long recipointsstatus = userLoggedOnSystem.getPoints();
                    precio = 9000;
                    if (recipointsstatus >= precio) {
                        RestarPtos(precio);
                        try {
                            enviarEmail();
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Toast.makeText(ShoppingUI.this, "NO CUENTA CON ECOPOINTS SUFICIENTES!!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                   hideLoadingIndicator();
                    DialogUtilities.showNoInternetDialog(ShoppingUI.this);
                }
            }
        });


    }
    private void showLoadingIndicator() {
        InterfacesUtilities.showLoadingIndicator(ShoppingUI.this, loadingLayout, loadingIndicator);
    }

    /**
     * Método para ocultar el indicador de carga.
     */
    public void hideLoadingIndicator() {
        InterfacesUtilities.hideLoadingIndicator(ShoppingUI.this, loadingLayout, loadingIndicator);
    }

    public void RestarPtos(int puntos) {
        //Obtener usuario logeado en sistema en general
        UserDTO systemUser = InterfacesUtilities.getLocalUser(getApplicationContext());
        //Recuperar ptos usuarios
        Long ptosactuales = systemUser.getPoints();
        ptosactuales -= puntos;
        //Actualizar ptos en usuario
        systemUser.setPoints(ptosactuales);
        //Creamos usuario DAO
        /*UserDAO userDAO = new UserDAOImpl();
        userDAO.setEntity(systemUser);
        typeChange = "restaptos";
        //Actualiza en firestore
        userDAO.save(new DataAccessUtilities.OnSavedListener() {
            @Override
            public void onSaveCompleted() {
                InterfacesUtilities.saveLocalUser(getApplicationContext(), systemUser);
                getPtos().setText(String.valueOf(systemUser.getPoints()));
                Toast.makeText(getApplicationContext(), "Recompensa Canjeada!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSaveError(String errorMessage) {
                Log.w("ERROR", errorMessage);
                Toast.makeText(getApplicationContext(), "Error al canjear recompensa.", Toast.LENGTH_SHORT).show();
//                hideLoadingIndicator();
            }
        });*/
    }

    private void enviarEmail() throws MessagingException {

        UserDTO userLoggedOnSystem = InterfacesUtilities.getLocalUser(ShoppingUI.this);
        String nombre = userLoggedOnSystem.getFullName();

        String destinatarioCorreo = userLoggedOnSystem.getEmail();

        String subject = "RECOMPENSA CANJEADA!!!!";
        String content = "Hola : " + nombre + "\n" + " Tu recompensa ah sido Canjeada Correctamente !!!" + "\n" +
                "Este es tu codigo:" + "834JGHF76HJDHX67834FVDASFSD";
        SendEmail.enviarMensaje(subject, content, destinatarioCorreo);
    }
}