package com.quiromarck.eco_ruta.repository;

import com.quiromarck.eco_ruta.entity.Driver;
import com.quiromarck.eco_ruta.entity.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByUserId(String userId);
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    List<Driver> findDriversByStatus(WorkStatus workStatus);
}