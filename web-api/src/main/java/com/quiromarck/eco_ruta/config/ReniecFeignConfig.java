package com.quiromarck.eco_ruta.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class ReniecFeignConfig implements RequestInterceptor {

    @Value("${reniec.api.token}") // Inyectar el token desde el archivo de configuración
    private String token;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Agrega el token al header solo para este cliente
        requestTemplate.header("Authorization", "Bearer " + token);
    }
}
