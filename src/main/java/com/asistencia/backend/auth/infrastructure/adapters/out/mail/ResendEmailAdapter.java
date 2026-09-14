package com.asistencia.backend.auth.infrastructure.adapters.out.mail;

import com.asistencia.backend.auth.domain.port.out.EmailSenderPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

// Adaptador de correo que implementa EmailSenderPort usando la API REST HTTPS de Resend
@Slf4j
@Component
public class ResendEmailAdapter implements EmailSenderPort {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from.email:UCSS Asistencia <onboarding@resend.dev>}")
    private String fromEmail;

    // Envia correo con credenciales de acceso usando la API HTTP de Resend
    @Override
    public void sendCredentialsEmail(String toEmail, String fullName, String generatedPassword) {
        log.info("CREDENCIALES GENERADAS PARA [{}]: password={}", toEmail, generatedPassword);

        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.error("No se ha configurado la clave resend.api.key. El correo a {} no sera enviado.", toEmail);
            return;
        }

        try {
            String htmlBody = buildCredentialsEmailTemplate(fullName, toEmail, generatedPassword);

            String jsonPayload = "{"
                    + "\"from\":\"" + escapeJson(fromEmail) + "\","
                    + "\"to\":[\"" + escapeJson(toEmail) + "\"],"
                    + "\"subject\":\"Credenciales de Acceso - Sistema de Asistencia UCSS\","
                    + "\"html\":\"" + escapeJson(htmlBody) + "\""
                    + "}";

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + apiKey.trim())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("Correo de credenciales enviado exitosamente a {} via Resend. Respuesta: {}", toEmail, response.body());
            } else if (response.statusCode() == 403) {
                log.warn("Resend rechazo el envio a {} (403 Forbidden): {}. Nota: El dominio de prueba de Resend solo envia correos a la cuenta registrada.", toEmail, response.body());
            } else {
                log.error("Error al enviar correo via Resend a {}. Codigo HTTP: {}, Respuesta: {}", toEmail, response.statusCode(), response.body());
            }
        } catch (Exception e) {
            log.warn("No se pudo enviar el correo via Resend a {}: {}. Las credenciales han quedado registradas en el sistema.", toEmail, e.getMessage());
        }
    }

    // Construye la plantilla HTML del correo
    private String buildCredentialsEmailTemplate(String fullName, String email, String password) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #0f172a; color: #f8fafc; margin: 0; padding: 20px; }"
                + ".container { max-width: 550px; margin: 0 auto; background-color: #1e293b; border-radius: 10px; padding: 30px; border: 1px solid #334155; }"
                + ".header { text-align: center; margin-bottom: 25px; }"
                + ".brand { font-size: 22px; font-weight: bold; letter-spacing: 2px; }"
                + ".brand-uix { color: #ffffff; }"
                + ".brand-cristian { color: #3b82f6; }"
                + ".title { font-size: 18px; color: #93c5fd; margin-top: 10px; }"
                + ".content { line-height: 1.6; color: #cbd5e1; font-size: 14px; }"
                + ".credentials-box { background-color: #0f172a; border: 1px dashed #3b82f6; border-radius: 8px; padding: 18px; margin: 20px 0; }"
                + ".cred-item { margin: 8px 0; font-size: 14px; }"
                + ".cred-label { color: #94a3b8; font-weight: bold; }"
                + ".cred-val { color: #38bdf8; font-family: monospace; font-size: 15px; }"
                + ".footer { text-align: center; margin-top: 25px; font-size: 12px; color: #64748b; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "  <div class='header'>"
                + "    <div class='brand'><span class='brand-uix'>UIX</span><span class='brand-cristian'>CRISTIAN</span></div>"
                + "    <div class='title'>Sistema de Asistencia Facial</div>"
                + "  </div>"
                + "  <div class='content'>"
                + "    <p>Estimado(a) <strong>" + fullName + "</strong>,</p>"
                + "    <p>Su cuenta de profesor ha sido creada exitosamente. A continuacion se detallan sus credenciales de acceso:</p>"
                + "    <div class='credentials-box'>"
                + "      <div class='cred-item'><span class='cred-label'>Correo:</span> <span class='cred-val'>" + email + "</span></div>"
                + "      <div class='cred-item'><span class='cred-label'>Contraseña temporal:</span> <span class='cred-val'>" + password + "</span></div>"
                + "    </div>"
                + "    <p>Por motivos de seguridad, le recomendamos cambiar su contraseña una vez que ingrese al sistema.</p>"
                + "  </div>"
                + "  <div class='footer'>"
                + "    <p>Mensaje generado automaticamente. Por favor no responder a este correo.</p>"
                + "  </div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    // Escapa caracteres especiales para construccion manual de JSON
    private String escapeJson(String value) {
        if (value == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 32) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }
}
