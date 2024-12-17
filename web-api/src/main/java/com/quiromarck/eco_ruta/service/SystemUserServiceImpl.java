package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.controllers.dto.SystemUserDTO;
import com.quiromarck.eco_ruta.controllers.dto.mappers.SystemUserMapper;
import com.quiromarck.eco_ruta.entity.SystemUser;
import com.quiromarck.eco_ruta.repository.SystemUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SystemUserServiceImpl implements SystemUserService {

    private final SystemUserRepository systemUserRepository;

    public SystemUserServiceImpl(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    @Override
    public SystemUser create(SystemUserDTO newUser) {
        SystemUser systemUser = SystemUserMapper.toEntity(newUser);
        return systemUserRepository.save(systemUser);
    }

    @Override
    public Optional<SystemUser> findByEmail(String email) {
        return systemUserRepository.findByEmail(email);
    }
}
