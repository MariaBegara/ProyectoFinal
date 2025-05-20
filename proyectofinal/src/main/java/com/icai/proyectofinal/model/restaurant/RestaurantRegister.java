package com.icai.proyectofinal.model.restaurant;

import com.icai.proyectofinal.model.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RestaurantRegister (
    @NotBlank(message = "Debe incluir el nombre del restaurante")
    String name_restaurant,

    @NotBlank(message = "El número de contacto es obligatorio")
    @Pattern(regexp = "^\\d{9}$", message = "El número debe tener 9 dígitos")
    String phone,

    //@Enumerated(EnumType.STRING)
    //@NotBlank(message = "Seleccione el tipo de comida de su restaurante")
    @NotNull(message = "Seleccione el tipo de comida de su restaurante")
    Type tipo,

    String direction,

    String latitude,

    String longitude,


    //@NotBlank(message = "Debe incluir un email")

    String email,

    //@NotBlank(message = "Debe incluir una contraseña")
    String password

){
}
