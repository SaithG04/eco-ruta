package com.qromarck.reciperu.DTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GenericMapper <T>{

    public static <T> T mapToDTO(Object data, Class<T> clazz) {
        // Inicializar Gson con opciones
        Gson gson = new GsonBuilder()
                .serializeNulls() // Incluye valores nulos
                .create();

        // Serializa el objeto genérico a JSON
        String json = gson.toJson(data);

        // Deserializa el JSON en el tipo especificado
        return gson.fromJson(json, clazz);
    }
}
