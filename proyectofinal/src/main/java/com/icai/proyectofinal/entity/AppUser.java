package com.icai.proyectofinal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Table(name = "USERS")
public class AppUser {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;


    @Column(nullable = false, unique = true)
    private String name_user;


    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String password;

    //@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public String role;

    @Column(nullable = false)
    public String name;

    @OneToMany(mappedBy = "owner")
    public List<AppRestaurant> ownedRestaurants;

    @OneToMany(mappedBy = "user")
    public List<AppReview> reviews;


    public void setId(String id) {
        this.id = id;
    }
    public String getName_user() {
        return name_user;
    }
    public void setName_user(String name_user) {
        this.name_user = name_user;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public java.util.List<AppRestaurant> getOwnedRestaurants() {
        return ownedRestaurants;
    }
    public void setOwnedRestaurants(java.util.List<AppRestaurant> ownedRestaurants) {
        this.ownedRestaurants = ownedRestaurants;
    }
    public java.util.List<AppReview> getReviews() {
        return reviews;
    }
    public void setReviews(java.util.List<AppReview> reviews) {
        this.reviews = reviews;
    }

}
