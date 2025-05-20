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

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public AppUser getOwner() {
        return owner;
    }
    public void setOwner(AppUser owner) {
        this.owner = owner;
    }
    public String getName_restaurant() {
        return name_restaurant;
    }
    public void setName_restaurant(String name_restaurant) {
        this.name_restaurant = name_restaurant;
    }
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }
}

