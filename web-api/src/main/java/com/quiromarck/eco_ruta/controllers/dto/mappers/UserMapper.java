package com.quiromarck.eco_ruta.controllers.dto.mappers;

import com.quiromarck.eco_ruta.controllers.dto.UserDTO;
import com.quiromarck.eco_ruta.entity.Status;
import com.quiromarck.eco_ruta.entity.User;

public class UserMapper {

    public static User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();

        // Mapear directamente sin necesidad de redundancias
        user.setUid(dto.getUid());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullname());
        user.setDni(dto.getDni());
        user.setIdDevice(dto.getIdDevice());
        user.setPhotoUrl(dto.getPhotoUrl());

        // Valores opcionales
        user.setPoints(dto.getPoints()); // Los valores nulos se mantienen como nulos
        user.setLastScanDate(dto.getLastScanDate());
        user.setRegistrationDate(dto.getRegistrationDate());
        user.setLocationId(dto.getLocationId());

        // Enum para estado
        user.setStatus(dto.getStatus() != null ? Status.valueOf(dto.getStatus()) : null);

        return user;
    }

    public static UserDTO toDto(User user) {
        if(user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();

        // Mapear directamente sin necesidad de redundancias
        userDTO.setUid(user.getUid());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullname(user.getFullName());
        userDTO.setDni(user.getDni());
        userDTO.setIdDevice(user.getIdDevice());
        userDTO.setPhotoUrl(user.getPhotoUrl());

        // Valores opcionales
        userDTO.setPoints(user.getPoints());
        userDTO.setLastScanDate(user.getLastScanDate());
        userDTO.setRegistrationDate(user.getRegistrationDate());
        userDTO.setLocationId(user.getLocationId());

        // Enum convertido a String
        userDTO.setStatus(user.getStatus() != null ? user.getStatus().name() : null);

        return userDTO;
    }
}
