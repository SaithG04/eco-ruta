package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.controllers.dto.UserDTO;
import com.quiromarck.eco_ruta.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Obtener todos los usuarios
    List<User> getAllUsers();

    // Guardar un nuevo usuario
    User createUser(UserDTO newUser);

    // Actualizar un usuario existente
    User updateUser(User updateUser);

    // Obtener un usuario por su ID (uid)
    Optional<User> getUserById(String uid);

    // Obtener un usuario por su correo electrónico
    UserDTO getUserByEmail(String email);

    Optional<User> getUserByDNI(String dni);

    // Eliminar un usuario por su ID (uid)
    void deleteUser(String uid);

    void validateUserDTO(UserDTO userDTO);

    void sendVerificationEmail(String email, String verificationLink);
}
