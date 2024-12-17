package com.quiromarck.eco_ruta.controllers.dto.mappers;

import com.quiromarck.eco_ruta.controllers.dto.DriverDTO;
import com.quiromarck.eco_ruta.entity.Driver;
import com.quiromarck.eco_ruta.entity.WorkStatus;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public static Driver toEntity(DriverDTO dto) {
        return Driver.builder()
                .userId(dto.getUserId())
                .licenseNumber(dto.getLicenseNumber())
                .phoneNumber(dto.getPhoneNumber())
                .vehicleType(dto.getVehicleType())
                .vehiclePlate(dto.getVehiclePlate())
                .status(WorkStatus.valueOf(dto.getStatus()))
                .build();
    }

    public static DriverDTO toDto(Driver driver) {
        return new DriverDTO(
                driver.getUserId(),
                driver.getLicenseNumber(),
                driver.getPhoneNumber(),
                driver.getVehicleType(),
                driver.getVehiclePlate(),
                String.valueOf(driver.getStatus())
        );
    }
}