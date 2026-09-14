package com.asistencia.backend.auth.infrastructure.adapters.out.mail;

import com.asistencia.backend.auth.domain.port.out.EmailSenderPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

// Adaptador de correo que implementa EmailSenderPort usando JavaMailSender y Gmail SMTP
@Slf4j
@Component
@RequiredArgsConstructor
public class GmailSmtpEmailAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // Envia correo con credenciales de acceso en formato HTML
    @Override
    public void sendCredentialsEmail(String toEmail, String fullName, String generatedPassword) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Credenciales de Acceso - Sistema de Asistencia UCSS");

            String htmlBody = buildCredentialsEmailTemplate(fullName, toEmail, generatedPassword);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            log.info("Correo de credenciales enviado exitosamente a {}", toEmail);
        } catch (MessagingException e) {
            log.error("Error al enviar correo electronico a {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Error al enviar el correo de credenciales: " + e.getMessage(), e);
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
}
