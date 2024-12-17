package com.quiromarck.eco_ruta.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    private int code;
    private String message;
    private Object data;
}