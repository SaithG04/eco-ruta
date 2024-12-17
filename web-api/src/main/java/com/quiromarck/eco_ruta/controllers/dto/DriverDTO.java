package com.quiromarck.eco_ruta.controllers.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {

    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("license_number")
    private String licenseNumber;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("vehicle_type")
    private String vehicleType;
    @JsonProperty("vehiclePlate")
    private String vehiclePlate;
    private String status;
}