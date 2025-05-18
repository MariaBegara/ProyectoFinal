package com.icai.proyectofinal.entity;

import com.icai.proyectofinal.model.Role;
import com.icai.proyectofinal.entity.AppRestaurant;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Entity
@Data
@Table(name = "USERS")
public class AppUser {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @Column(nullable = false, unique = true)
    public String name_user;

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
