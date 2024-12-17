package com.quiromarck.eco_ruta.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String uid;
    private String email;
    @JsonProperty("full_name")
    private String fullname;
    private String dni;
    @JsonProperty("id_device")
    private String idDevice;
    @JsonProperty("last_scan_date")
    private Date lastScanDate;
    private Long points;
    @JsonProperty("registration_date")
    private Date registrationDate;
    private String status;
    @JsonProperty("photo_url")
    private String photoUrl;
    @JsonProperty("location_id")
    private Long locationId;

    public UserDTO(String uid, String email, String fullname, String dni, String idDevice, String photoUrl, Long locationId) {
        this.uid = uid;
        this.email = email;
        this.fullname = fullname;
        this.dni = dni;
        this.idDevice = idDevice;
        this.photoUrl = photoUrl;
        this.registrationDate = new Date();
        this.points = 0L;
        this.lastScanDate = null;
        this.status = "ACTIVE";
        this.locationId = locationId;
    }
}