package com.qromarck.reciperu.DTO;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ReportDTO {

    private Double latitude;
    private Double longitude;
    private String description;
    @SerializedName("report_date")
    private String reportDate;
    private String status;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("photo_url")
    private String photoUrl;

    // Constructor
    public ReportDTO(Double latitude, Double longitude, String description, String reportDate, String status, String userId, String photoUrl) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.reportDate = reportDate;
        this.status = status;
        this.userId = userId;
        this.photoUrl = photoUrl;
    }

    public ReportDTO() {
    }

    // Getters y Setters
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReportDTO{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", reportDate='" + reportDate + '\'' +
                ", status='" + status + '\'' +
                ", userId='" + userId + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
