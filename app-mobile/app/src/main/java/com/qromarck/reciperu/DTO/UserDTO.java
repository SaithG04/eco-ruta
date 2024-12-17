// UserDTO.java
package com.qromarck.reciperu.DTO;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class UserDTO implements Serializable {
    private String uid;
    private String email;
    @SerializedName("full_name")
    private String fullName;
    private String dni;
    @SerializedName("id_device")
    private String idDevice;
    @SerializedName("last_scan_date")
    private Date lastScanDate;
    private Long points;
    @SerializedName("registration_date")
    private Date registrationDate;
    private String status;
    @SerializedName("photo_url")
    private String photoUrl;
    @SerializedName("location_id")
    private Long locationId;

    // Empty constructor
    public UserDTO() {
    }

    public UserDTO(String uid, String email, String fullName, String dni, String idDevice, Date lastScanDate, Long points, Date registrationDate, String status, String photoUrl, Long locationId) {
        this.uid = uid;
        this.email = email;
        this.fullName = fullName;
        this.dni = dni;
        this.idDevice = idDevice;
        this.lastScanDate = lastScanDate;
        this.points = points;
        this.registrationDate = registrationDate;
        this.status = status;
        this.photoUrl = photoUrl;
        this.locationId = locationId;
    }

    public UserDTO(String fullName, String numeroDocumento) {
        this.fullName = fullName;
        this.dni = numeroDocumento;
    }

    // Getters and Setters

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public Date getLastScanDate() {
        return lastScanDate;
    }

    public void setLastScanDate(Date lastScanDate) {
        this.lastScanDate = lastScanDate;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserDTO{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dni='" + dni + '\'' +
                ", idDevice='" + idDevice + '\'' +
                ", lastScanDate=" + lastScanDate +
                ", points=" + points +
                ", registrationDate=" + registrationDate +
                ", status='" + status + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", locationId=" + locationId +
                '}';
    }
}
