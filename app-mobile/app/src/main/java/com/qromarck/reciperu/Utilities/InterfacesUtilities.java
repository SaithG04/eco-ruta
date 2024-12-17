package com.qromarck.reciperu.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.Gson;
import com.qromarck.reciperu.DTO.DriverDTO;
import com.qromarck.reciperu.DTO.UserDTO;
import com.qromarck.reciperu.R;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfacesUtilities {

    private static final String PREFERENCES_NAME = "app_preferences";
    private static final String KEY_USER = "user";
    private static final String KEY_DRIVER = "driver";
    private static final String KEY_DRIVER_ID = "driver_id";

    public static <T> Map<String, Object> entityToMap(T entity) {
        Map<String, Object> resultMap = new HashMap<>();
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if(field.getType().getSimpleName().equals("Timestamp") && value == null){
                    resultMap.put(field.getName(), FieldValue.serverTimestamp());
                }else{
                    resultMap.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.out);
            }
        }
        return resultMap;
    }

    public static String[] obtenerInfoAtributo(Object objeto, Object valorAtributo) {
        Class<?> clazz = objeto.getClass();
        String nombreCampo = null;
        String claseCampo = null;

        try {
            // Iterar sobre los campos de la clase para encontrar el que coincide con el valor del atributo
            for (Field campo : clazz.getDeclaredFields()) {
                campo.setAccessible(true); // Permitir el acceso a campos privados
                Object valor = campo.get(objeto);
                if (valorAtributo.equals(valor)) {
                    nombreCampo = campo.getName();
                    claseCampo = campo.getType().getSimpleName();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return new String[]{claseCampo, nombreCampo};
    }

    public static Object[] mapToArray(Map<String, Object> map) {
        Object[] array = new Object[map.size() * 2];
        int index = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            array[index++] = entry.getKey();
            array[index++] = entry.getValue();
        }
        return array;
    }

    public static <T> Object[] entityToArray(T entity) {
        Map<String, Object> resultMap = entityToMap(entity);
        return mapToArray(resultMap);
    }

    public static void showLoadingIndicator(Activity activity, FrameLayout loadingLayout, ProgressBar loadingIndicator) {
        loadingIndicator.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.VISIBLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void hideLoadingIndicator(Activity activity, FrameLayout loadingLayout, ProgressBar loadingIndicator) {
        loadingIndicator.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Método para guardar un objeto Usuario en SharedPreferences
    public static void saveLocalUser(Context context, UserDTO userDTO) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (userDTO != null) {
            // Convertir el objeto Usuario a JSON
            Gson gson = new Gson();
            String usuarioJson = gson.toJson(userDTO);

            // Guardar el JSON en SharedPreferences
            editor.putString(KEY_USER, usuarioJson);
        } else {
            // Si el usuario es null, guardar una cadena vacía en SharedPreferences
            editor.putString(KEY_USER, "");
        }

        editor.apply();
    }

    public static void saveLocalDriver(Context context, DriverDTO driverDTO){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (driverDTO != null) {
            // Convertir el objeto a JSON
            Gson gson = new Gson();
            String usuarioJson = gson.toJson(driverDTO);

            // Guardar el JSON en SharedPreferences
            editor.putString(KEY_DRIVER, usuarioJson);
        } else {
            // Si el usuario es null, guardar una cadena vacía en SharedPreferences
            editor.putString(KEY_DRIVER, "");
        }

        editor.apply();
    }

    public static void saveDriverId(Context context, String idDriver){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (idDriver != null) {
            // Guardar en SharedPreferences
            editor.putString(KEY_DRIVER_ID, idDriver);
        } else {
            // Si el usuario es null, guardar una cadena vacía en SharedPreferences
            editor.putString(KEY_DRIVER_ID, "");
        }
        editor.apply();
    }


    // Método para recuperar un objeto Usuario de SharedPreferences
    public static UserDTO getLocalUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        // Obtener el JSON del usuario guardado en SharedPreferences
        String userJson = sharedPreferences.getString(KEY_USER, null);

        // Convertir el JSON de vuelta a un objeto Usuario
        Gson gson = new Gson();

        return gson.fromJson(userJson, UserDTO.class);
    }

    public static DriverDTO getLocalDriver(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String driverJson = sharedPreferences.getString(KEY_DRIVER, null);
        Gson gson = new Gson();
        return gson.fromJson(driverJson, DriverDTO.class);
    }

    public static String getDriverId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DRIVER_ID, null);
    }

    public static String byteArrayToString(byte[] array){
        return Base64.encodeToString(array, Base64.DEFAULT);
    }

    public static byte[] stringToByteArray(String string){
        return Base64.decode(string, Base64.DEFAULT);
    }

    public static int hashPassword(String password, int salt) {
        try {
            // Convertir el salt (entero) en un arreglo de bytes
            byte[] saltBytes = ByteBuffer.allocate(4).putInt(salt).array();

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(saltBytes);
            byte[] passwordBytes = md.digest(password.getBytes());

            // Convertir los primeros 4 bytes del resultado hash a un entero
            int hashedPassword = ByteBuffer.wrap(passwordBytes).getInt();

            return hashedPassword;
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static int generateSalt() {
        // Generar un salt aleatorio como un entero
        SecureRandom random = new SecureRandom();
        return random.nextInt();
    }
    public static byte[] intToBytes(int value) {
        // Crear un ByteBuffer de tamaño 4 (tamaño de un entero en bytes)
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);

        // Colocar el entero en el ByteBuffer (big-endian por defecto)
        buffer.putInt(value);

        // Obtener el arreglo de bytes desde el ByteBuffer
        return buffer.array();
    }
    public static int[] getDayMonthYear(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp.toDate());

        int[] dayMonthYear = new int[3];
        dayMonthYear[0] = calendar.get(Calendar.DAY_OF_MONTH); // Día
        dayMonthYear[1] = calendar.get(Calendar.MONTH) + 1; // Mes (se suma 1 porque en Calendar los meses van de 0 a 11)
        dayMonthYear[2] = calendar.get(Calendar.YEAR); // Año

        return dayMonthYear;
    }

    public static boolean compareDatesWithoutHMS(Timestamp date1, Timestamp date2) {
        int[] dayMonthYear1 = getDayMonthYear(date1);
        int[] dayMonthYear2 = getDayMonthYear(date2);

        // Comparar los valores de día, mes y año
        return Arrays.equals(dayMonthYear1, dayMonthYear2);
    }

    // Método para validar el formato del correo electrónico usando expresión regular
    public static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    @Deprecated
    public static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    @Deprecated
    public String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    @Deprecated
    @RequiresApi(api = Build.VERSION_CODES.O)
    public <T> Object[] entityToObjectArray(T entity) {
        if (entity instanceof UserDTO) {
            //Long id = ((UserDTO) entity).getId();
            String nombre = ((UserDTO) entity).getFullName();
            String correo = ((UserDTO) entity).getEmail();
//            byte[] hashedPassword = ((Usuario) entity).getHashedPassword();
//            byte[] salt = ((Usuario) entity).getSalt();
            String status = ((UserDTO) entity).getStatus();

//            String hashedPasswordString = bytesToHex(hashedPassword);
//            String saltString = bytesToHex(salt);

            return new Object[]{
                    //id,
                    "",
                    nombre,
                    correo,
//                    hashedPasswordString,
//                    saltString,
                    status
            };
        }
        return null;
    }

    public static boolean isGoogleEmail(String email) {
        // Lista de dominios conocidos de Google
        List<String> googleDomains = Arrays.asList(
                "gmail.com",
                "googlemail.com",
                "google.com",
                "google.co.uk",
                "google.ca",
                "google.com.au",
                "google.de",
                "google.fr",
                "google.it",
                "google.es",
                "google.nl",
                "google.se",
                "google.no",
                "google.fi",
                "google.dk",
                "google.be",
                "google.ch",
                "google.at",
                "google.ie",
                "google.pt",
                "google.pl",
                "google.gr",
                "google.cz",
                "google.hu",
                "google.ro",
                "google.bg",
                "google.lt",
                "google.lv",
                "google.ee",
                "google.ru",
                "google.ua",
                "google.by",
                "google.kz",
                "google.co.in",
                "google.com.pk",
                "google.com.sa",
                "google.ae",
                "google.co.jp",
                "google.com.hk",
                "google.com.sg",
                "google.com.my",
                "google.com.ph",
                "google.co.id",
                "google.co.kr",
                "google.co.th",
                "google.com.vn",
                "google.com.tw",
                "google.co.za",
                "google.co.ke",
                "google.co.ug",
                "google.co.tz",
                "google.co.ma",
                "google.co.ng",
                "google.co.et",
                "google.co.gh",
                "google.co.tn",
                "google.co.il",
                "google.co.ir",
                "google.com.br",
                "google.com.ar",
                "google.com.mx",
                "google.cl",
                "google.com.co",
                "google.com.pe",
                "google.com.uy",
                "google.com.py",
                "google.com.bo",
                "google.com.ec",
                "google.com.ve",
                "ucvvirtual.edu.pe"
        );
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        return googleDomains.contains(domain);
    }

    public static boolean isDniValid(String dni) {
        return dni.matches("\\d{8}");
    }

    public static void showDefaultError(Context context, String tag, String messageLog) {
        Log.e(tag, messageLog);
        Toast.makeText(context, "Lo sentimos, ha ocurrido un error.", Toast.LENGTH_SHORT).show();
    }

    public static void showMessage(Context context, String tag, String toastMessage) {
        Log.i(tag, toastMessage);
        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
    }

    public static void showError(Context context, String tag, String messageLog, String messageToast) {
        Log.e(tag, messageLog);
        Toast.makeText(context, messageToast, Toast.LENGTH_LONG).show();
    }

    public static String getApiBackUrl(Context context) {
        return context.getString(R.string.WEB_API_URL);
    }

    public static Bitmap convertToBitmap(byte[] imageData) {
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }
}
