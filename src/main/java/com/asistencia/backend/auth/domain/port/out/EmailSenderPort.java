package com.asistencia.backend.auth.domain.port.out;

// Puerto de salida para envio de correos electronicos
public interface EmailSenderPort {
    void sendCredentialsEmail(String toEmail, String fullName, String generatedPassword);
}
