package com.asistencia.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class AssistanceBackendApplication {

	@PostConstruct
	public void init() {
		// Fija la zona horaria del servidor a Peru (America/Lima, UTC-5) para despliegues cloud
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
	}

	public static void main(String[] args) {
		SpringApplication.run(AssistanceBackendApplication.class, args);
	}

}
