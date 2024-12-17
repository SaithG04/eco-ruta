package com.quiromarck.eco_ruta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;
    private double longitude;
    private String description;
    @Column(nullable = false)
    private Date reportDate;
    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private String userId;
    @Lob
    private String photoUrl;

    @PrePersist
    private void prePersist() {
        reportDate = new Date();
    }
}