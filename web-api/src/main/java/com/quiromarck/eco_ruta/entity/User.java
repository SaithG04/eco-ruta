package com.quiromarck.eco_ruta.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "users_app")
public class User {

    @Id
    private String uid;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String fullName;
    @Column(unique = true, nullable = false)
    private String dni;
    @Column(unique = true)
    private String idDevice;
    private Date lastScanDate;
    private Long points;
    @Column(nullable = false)
    private Date registrationDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    private String photoUrl;
    private Long locationId;

    @PrePersist
    private void prePersist() {
        if (this.uid == null) {
            this.uid = UUID.randomUUID().toString();
        }
        registrationDate = new Date();
    }

}