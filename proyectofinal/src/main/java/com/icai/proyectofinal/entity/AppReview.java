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
    private float score; // entre 1 y 5

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public AppUser getUser() {
        return user;
    }
    public void setUser(AppUser user) {
        this.user = user;
    }
    public AppRestaurant getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(AppRestaurant restaurant) {
        this.restaurant = restaurant;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }
}
