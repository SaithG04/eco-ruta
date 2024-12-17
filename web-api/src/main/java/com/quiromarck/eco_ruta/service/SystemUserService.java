package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.controllers.dto.SystemUserDTO;
import com.quiromarck.eco_ruta.entity.SystemUser;

import java.util.Optional;

public interface SystemUserService {
    // Guardar un nuevo usuario de sistema
    SystemUser create(SystemUserDTO newUser);

    Optional<SystemUser> findByEmail(String email);
}
