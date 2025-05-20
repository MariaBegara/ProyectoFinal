package com.icai.proyectofinal.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Table(name = "TOKENS")
public class Token {

    @Id@GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public AppUser appUser;

}
