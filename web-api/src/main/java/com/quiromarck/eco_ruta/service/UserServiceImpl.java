package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.controllers.dto.UserDTO;
import com.quiromarck.eco_ruta.controllers.dto.mappers.UserMapper;
import com.quiromarck.eco_ruta.entity.User;
import com.quiromarck.eco_ruta.exception.InvalidInputException;
import com.quiromarck.eco_ruta.exception.UserNotFoundException;
import com.quiromarck.eco_ruta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail; // Extrae el remitente desde properties

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(UserDTO newUserDTO) {
        newUserDTO.setPoints(0L);
        newUserDTO.setRegistrationDate(new Date());
        newUserDTO.setStatus("ACTIVE");
        User newUser = UserMapper.toEntity(newUserDTO);
        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(User updateUser) {
        // Buscamos el usuario por su UID
        User existingUser = userRepository.findById(updateUser.getUid()).orElseThrow(
                () -> new UserNotFoundException("Usuario no encontrado con uid: " + updateUser.getUid())
        );

        // Actualizamos los campos del usuario con los nuevos valores
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setFullName(updateUser.getFullName());
        existingUser.setDni(updateUser.getDni());
        existingUser.setIdDevice(updateUser.getIdDevice());
        existingUser.setLastScanDate(updateUser.getLastScanDate());
        existingUser.setPoints(updateUser.getPoints());
        existingUser.setStatus(updateUser.getStatus());
        existingUser.setPhotoUrl(updateUser.getPhotoUrl());

        // Guardamos el usuario actualizado
        return userRepository.save(existingUser);
    }

    @Override
    public Optional<User> getUserById(String uid) {
        return userRepository.findById(uid);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return UserMapper.toDto(userRepository
                .findByEmail(email).orElseThrow(() -> new UserNotFoundException("There is no user with this email.")));
    }

    @Override
    public Optional<User> getUserByDNI(String dni) {
        return userRepository.findByDniEquals(dni);
    }

    @Override
    public void deleteUser(String uid) {
        User existingUser = userRepository.findById(uid).orElseThrow(
                UserNotFoundException::new
        );
        userRepository.delete(existingUser);
    }

    @Override
    public void validateUserDTO(UserDTO userDTO) {
        if (userDTO == null) {
            throw new InvalidInputException("UserDTO cannot be null");
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            throw new InvalidInputException("Email is required and cannot be null or empty");
        }
        if (userDTO.getFullname() == null || userDTO.getFullname().isEmpty()) {
            throw new InvalidInputException("Fullname is required and cannot be null or empty");
        }
        if (userDTO.getDni() == null || userDTO.getDni().isEmpty()) {
            throw new InvalidInputException("DNI is required and cannot be null or empty");
        }
        if (userDTO.getIdDevice() == null || userDTO.getIdDevice().isEmpty()) {
            throw new InvalidInputException("Device ID is required and cannot be null or empty");
        }
    }

    @Override
    public void sendVerificationEmail(String email, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify your email address");
        message.setText("Click the following link to verify your email: " + verificationLink);
        message.setFrom(senderEmail);

        mailSender.send(message);
    }
}
