package com.quiromarck.eco_ruta.controllers.dto.mappers;

import com.quiromarck.eco_ruta.controllers.dto.SystemUserDTO;
import com.quiromarck.eco_ruta.entity.SystemUser;
import com.quiromarck.eco_ruta.entity.SystemUserStatus;

public class SystemUserMapper {
    // Convertir de DTO a Entity
    public static SystemUser toEntity(SystemUserDTO dto) {
        if (dto == null) {
            return null;
        }
        SystemUser systemUser = new SystemUser();
        systemUser.setId(dto.getId());
        systemUser.setUsername(dto.getUsername());
        systemUser.setEmail(dto.getEmail());
        systemUser.setStatus(SystemUserStatus.valueOf(dto.getStatus().toUpperCase()));
        return systemUser;
    }

    // Convertir de Entity a DTO
    public static SystemUserDTO toDTO(SystemUser entity) {
        if (entity == null) {
            return null;
        }
        SystemUserDTO dto = new SystemUserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }
}
