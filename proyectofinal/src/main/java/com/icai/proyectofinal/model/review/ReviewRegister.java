package com.icai.proyectofinal.model.review;

import com.icai.proyectofinal.model.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ReviewRegister(
        @NotBlank(message = "Debe incluir el nombre del restaurante")
        String name_restaurant,

        @NotBlank(message = "El número de contacto es obligatorio")
        @Pattern(regexp = "^\\d{9}$", message = "El número debe tener 9 dígitos")
        String phone,

        @Enumerated(EnumType.STRING)
        @NotBlank(message = "Seleccione el tipo de comida de su restaurante")
        Type tipo,

        String direction,

        String latitude,

        String longitude

) {
}


