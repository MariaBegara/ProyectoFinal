package com.icai.proyectofinal.model.review;

import jakarta.validation.constraints.NotBlank;

public record ReviewRegister(
        @NotBlank(message = "Cuéntanos tu experiencia aquí")
        String content,

        float score

) {
}


