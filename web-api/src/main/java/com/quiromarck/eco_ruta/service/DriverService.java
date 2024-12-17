package com.quiromarck.eco_ruta.service;

import com.quiromarck.eco_ruta.entity.Driver;
import com.quiromarck.eco_ruta.entity.WorkStatus;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    Driver registerDriver(Driver driver);

    Optional<Driver> getDriverByUserId(String userId);

    List<Driver> getDriversByStatus(WorkStatus status);

    Optional<Driver> getDriverByStatusAndLocationId(WorkStatus status, Long locationId);
}
