package com.icai.proyectofinal.model;

import org.hibernate.annotations.processing.Pattern;

public record ProfileRequest(
    String name,

    Role role,

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{8,}$")
    String password
) {}
