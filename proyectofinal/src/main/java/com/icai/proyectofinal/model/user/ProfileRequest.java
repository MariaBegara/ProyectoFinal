package com.icai.proyectofinal.model.user;

//import org.hibernate.annotations.processing.Pattern;
import com.icai.proyectofinal.model.Role;
import jakarta.validation.constraints.Pattern;


public record ProfileRequest(
    String name,

    Role role,

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{8,}$")
    String password
) {}
