package com.quiromarck.eco_ruta.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    private Long id;
    private double latitude;
    private double longitude;
    private String description;
    @JsonProperty("report_date")
    private Date reportDate;
    private String status;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("photo_url")
    private String photoUrl;
}