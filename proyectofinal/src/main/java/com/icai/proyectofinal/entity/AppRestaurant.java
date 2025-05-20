package com.icai.proyectofinal.entity;

import com.icai.proyectofinal.model.Type;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
//@Table(name = "RESTAURANTS")
public class AppRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_owner", nullable = false)
    private AppUser owner;

    @Column(nullable = false)
    private String name_restaurant;

    @Column(nullable = false)
    private String direction;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private float score;
}