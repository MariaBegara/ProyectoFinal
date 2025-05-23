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

}
