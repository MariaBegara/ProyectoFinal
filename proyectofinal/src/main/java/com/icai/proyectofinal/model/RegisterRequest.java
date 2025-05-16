package com.icai.proyectofinal.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "El usuario no se puede crear sin nombre")
        String name_user,

        @Email(message = "El formato del email es incorrecto")
        @NotBlank(message = "El email es obligatorio")
        String email,

        @NotBlank(message = "Introduzca una contraseña válida")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{8,}$")
        String password1,

        @NotBlank(message = "Las contraseñas no coinciden")
        String password2,

        String name,

        String role

) {
}