package com.icai.proyectofinal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "RESTAURANTS")
public class AppRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_owner", nullable = false)
    private AppUser owner;

    @Column(nullable = false)
    private String name_restaurant;

    @Column(nullable = false, unique = true)
    private String direction;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

}
