package com.quiromarck.eco_ruta.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        // Ruta al archivo JSON de servicio de Firebase
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase/firebase-service-account.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://reciperu-9ab32-default-rtdb.firebaseio.com")
                .build();

        if (FirebaseApp.getApps().isEmpty()) { // Evita inicializar Firebase más de una vez
            FirebaseApp.initializeApp(options);
        }
        return FirebaseAuth.getInstance();
    }
}