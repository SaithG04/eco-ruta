package com.quiromarck.eco_ruta.controllers;

import com.quiromarck.eco_ruta.controllers.dto.DriverDTO;
import com.quiromarck.eco_ruta.controllers.dto.mappers.DriverMapper;
import com.quiromarck.eco_ruta.entity.Driver;
import com.quiromarck.eco_ruta.entity.WorkStatus;
import com.quiromarck.eco_ruta.service.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerDriver(@RequestBody DriverDTO driverDTO) {
        try {
            Driver driver = DriverMapper.toEntity(driverDTO);
            Driver registeredDriver = driverService.registerDriver(driver);
            DriverDTO responseDTO = DriverMapper.toDto(registeredDriver);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(HttpStatus.CREATED.value(), "Driver registered successfully", responseDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null));
        }
    }

    @GetMapping("/findByUserUid/{userId}")
    public ResponseEntity<ApiResponse> getDriverByUserId(@PathVariable String userId) {
        return driverService.getDriverByUserId(userId)
                .map(driver -> {
                    DriverDTO driverDTO = DriverMapper.toDto(driver);
                    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Driver found", driverDTO));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), "Driver not found", null)));
    }

    @GetMapping("/findByStatus/{status}")
    public ResponseEntity<ApiResponse> getDriversByStatus(@PathVariable WorkStatus status) {

        List<DriverDTO> driverDTOS = new ArrayList<>();
        driverService.getDriversByStatus(status).forEach(driver -> {
                    DriverDTO driverDTO = DriverMapper.toDto(driver);
                    driverDTOS.add(driverDTO);
                });

        if(driverDTOS.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), "Drivers not found", null));
        }else {
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Driver found", driverDTOS));
        }
    }

    @GetMapping("/findByStatus/{status}/{locationId}")
    public ResponseEntity<ApiResponse> getDriverByStatusAndLocationId(@PathVariable WorkStatus status, @PathVariable Long locationId) {

        return driverService.getDriverByStatusAndLocationId(status, locationId)
                .map(driver -> {
                    DriverDTO driverDTO = DriverMapper.toDto(driver);
                    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Driver found", driverDTO));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), "Driver not found", null)));
    }
}