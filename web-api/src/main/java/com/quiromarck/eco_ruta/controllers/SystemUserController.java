package com.quiromarck.eco_ruta.controllers;

import com.quiromarck.eco_ruta.controllers.dto.DriverDTO;
import com.quiromarck.eco_ruta.controllers.dto.SystemUserDTO;
import com.quiromarck.eco_ruta.controllers.dto.mappers.SystemUserMapper;
import com.quiromarck.eco_ruta.service.SystemUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/systemUsers")
public class SystemUserController {

    private final SystemUserService systemUserService;

    public SystemUserController(SystemUserService systemUserService) {
        this.systemUserService = systemUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerDriver(@RequestBody DriverDTO driverDTO) {
        /*try {
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
        }*/
        return null;
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<ApiResponse> getByEmail(@RequestParam String email) {

        return systemUserService.findByEmail(email)
                .map(systemUser -> {
                    SystemUserDTO driverDTO = SystemUserMapper.toDTO(systemUser);
                    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "System user found", driverDTO));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), "System user not found", null)));
    }
}
