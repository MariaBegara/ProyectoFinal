package com.icai.proyectofinal.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "TOKENS")
public class Token {

    @Id@GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public AppUser appUser;

    public String getId() {
        return id;
    }

}
