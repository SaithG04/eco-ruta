package com.quiromarck.eco_ruta.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SystemUserDTO {
    private Long id;
    private String username;
    private String email;
    private String status;
}
