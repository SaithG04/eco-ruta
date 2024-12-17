package com.quiromarck.eco_ruta.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite CORS para todas las rutas y orígenes
        registry.addMapping("/**")
                .allowedOrigins("*")  // Aquí puedes poner los orígenes permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Métodos permitidos
                .allowedHeaders("*"); // Permite todos los encabezados
                //.allowCredentials(true);  // Si es necesario permitir cookies y credenciales
    }
}
