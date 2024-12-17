package com.qromarck.reciperu.Utilities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class GeoCoderHelper {
    private static final String TAG = "CityFinder";

    public static String getCityFromCoordinates(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            // Obtener la dirección de las coordenadas
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                // Extraer el nombre de la ciudad
                Address address = addresses.get(0);
                return address.getLocality(); // Nombre de la ciudad
            } else {
                Log.e(TAG, "No se encontraron direcciones para las coordenadas proporcionadas.");
            }
        } catch (Exception e){
            Log.e(TAG, "Error al obtener la ciudad desde las coordenadas.", e);
        }
        return "Ciudad desconocida";
    }
}
