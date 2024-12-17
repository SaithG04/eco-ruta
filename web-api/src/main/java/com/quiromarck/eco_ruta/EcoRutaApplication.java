package com.quiromarck.eco_ruta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EcoRutaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcoRutaApplication.class, args);
	}
}