package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.entity.Driver;
import com.quiromarck.eco_ruta.entity.WorkStatus;
import com.quiromarck.eco_ruta.exception.InvalidInputException;
import com.quiromarck.eco_ruta.repository.DriverRepository;
import com.quiromarck.eco_ruta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository, UserRepository userRepository) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Driver registerDriver(Driver driver) {
        if (driverRepository.findByUserId(driver.getUserId()).isPresent()) {
            throw new InvalidInputException("Email already registered");
        }
        if (driverRepository.findByLicenseNumber(driver.getLicenseNumber()).isPresent()) {
            throw new InvalidInputException("License number already registered");
        }

        return driverRepository.save(driver);
    }

    @Override
    public Optional<Driver> getDriverByUserId(String userId) {
        return driverRepository.findByUserId(userId);
    }

    @Override
    public List<Driver> getDriversByStatus(WorkStatus status) {
        return driverRepository.findDriversByStatus(status);
    }

    @Override
    public Optional<Driver> getDriverByStatusAndLocationId(WorkStatus status, Long locationId) {
        return driverRepository.findDriversByStatus(status).stream()
                .filter(driver -> {
                    String userId = driver.getUserId();
                    return userRepository.findById(userId)
                            .map(user -> Objects.equals(user.getLocationId(), locationId))
                            .orElse(false);
                })
                .findFirst();
    }

}