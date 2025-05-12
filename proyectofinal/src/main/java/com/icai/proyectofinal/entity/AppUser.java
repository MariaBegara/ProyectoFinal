package com.icai.proyectofinal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @Column(nullable = false, unique = true)
    public String name_user;

    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public String role;

    @Column(nullable = false)
    public String name;

}
