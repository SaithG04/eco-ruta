package com.quiromarck.eco_ruta.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "codes_qr")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Qr {
    @Id
    private String id;
    private byte[] hashedCode;
    private int pointsValue;
    private byte[] saltCode;
    private Long locationId;

    @PrePersist
    public void prePersist(){
        if(id == null){
            id = UUID.randomUUID().toString();
        }
    }
}
