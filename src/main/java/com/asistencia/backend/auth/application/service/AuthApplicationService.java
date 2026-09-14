package com.asistencia.backend.auth.application.service;

import com.asistencia.backend.auth.application.dto.AuthResponseDto;
import com.asistencia.backend.auth.application.dto.LoginRequestDto;
import com.asistencia.backend.auth.application.dto.RegisterProfessorRequestDto;
import com.asistencia.backend.auth.domain.model.Role;
import com.asistencia.backend.auth.domain.model.User;
import com.asistencia.backend.auth.domain.port.in.AuthUseCase;
import com.asistencia.backend.auth.domain.port.out.EmailSenderPort;
import com.asistencia.backend.auth.domain.port.out.PasswordEncoderPort;
import com.asistencia.backend.auth.domain.port.out.TokenProviderPort;
import com.asistencia.backend.auth.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

// Servicio de aplicacion que implementa la logica de autenticacion y registro
@Service
@RequiredArgsConstructor
public class AuthApplicationService implements AuthUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final EmailSenderPort emailSenderPort;
    private final TokenProviderPort tokenProviderPort;

    // Autentica al usuario y retorna el token JWT
    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto request) {
        User user = userRepositoryPort.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));

        if (!user.isActive()) {
            throw new IllegalStateException("La cuenta de usuario se encuentra desactivada");
        }

        if (!passwordEncoderPort.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        String token = tokenProviderPort.generateToken(user);

        return AuthResponseDto.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .profilePicture(user.getProfilePicture())
                .message("Inicio de sesion exitoso")
                .build();
    }

    // Obtiene el perfil actual del usuario autenticado
    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto getProfile(String email) {
        User user = userRepositoryPort.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con correo: " + email));

        return AuthResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .profilePicture(user.getProfilePicture())
                .build();
    }

    // Actualiza el nombre y foto de perfil del usuario autenticado
    @Override
    @Transactional
    public AuthResponseDto updateProfile(com.asistencia.backend.auth.application.dto.UpdateProfileRequestDto request, String email) {
        User user = userRepositoryPort.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con correo: " + email));

        user.setFullName(request.getFullName().trim());
        if (request.getProfilePicture() != null) {
            user.setProfilePicture(request.getProfilePicture());
        }

        User saved = userRepositoryPort.save(user);

        return AuthResponseDto.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .role(saved.getRole().name())
                .profilePicture(saved.getProfilePicture())
                .message("Perfil actualizado exitosamente")
                .build();
    }

    // Registra un nuevo profesor con contraseña autogenerada y envio por correo
    @Override
    @Transactional
    public AuthResponseDto registerProfessor(RegisterProfessorRequestDto request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepositoryPort.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("El correo electronico ya se encuentra registrado");
        }

        String generatedPassword = generateSecurePassword(10);
        String encodedPassword = passwordEncoderPort.encode(generatedPassword);

        User newUser = User.builder()
                .fullName(request.getFullName().trim())
                .email(normalizedEmail)
                .password(encodedPassword)
                .role(Role.ROLE_PROFESSOR)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepositoryPort.save(newUser);

        // Envia las credenciales de acceso al correo real del profesor
        emailSenderPort.sendCredentialsEmail(savedUser.getEmail(), savedUser.getFullName(), generatedPassword);

        return AuthResponseDto.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .message("Profesor registrado exitosamente. Las credenciales fueron enviadas a su correo.")
                .build();
    }

    // Genera una contraseña aleatoria y segura
    private String generateSecurePassword(int length) {
        final String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String lowercase = "abcdefghijklmnopqrstuvwxyz";
        final String digits = "0123456789";
        final String special = "!@#$%*";
        final String allChars = uppercase + lowercase + digits + special;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Mezcla los caracteres generados
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        return new String(passwordArray);
    }
}
