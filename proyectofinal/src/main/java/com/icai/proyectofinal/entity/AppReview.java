package com.icai.proyectofinal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
// Solo se permite una reseña por usuario por restaurante
@Table(name = "REVIEWS", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_restaurant", "id_user"})
})
public class AppReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private AppUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_restaurant", nullable = false)
    private AppRestaurant restaurant;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String score; // entre 1 y 5

}
