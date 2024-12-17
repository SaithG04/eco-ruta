package com.quiromarck.eco_ruta.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {

    @Autowired
    private FirebaseAuth firebaseAuth;

    // Verificar el token de Firebase
    public FirebaseToken verifyIdToken(String idToken) throws Exception {
        return firebaseAuth.verifyIdToken(idToken);
    }
}
