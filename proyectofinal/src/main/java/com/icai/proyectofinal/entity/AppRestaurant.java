package com.icai.proyectofinal.entity;

import com.icai.proyectofinal.model.Type;
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

    //cambio lo de unique=true para pruebas
    @Column(nullable = false, unique = false)
    private String direction;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;


    //setters
    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public void setName_restaurant(String name_restaurant) {
        this.name_restaurant = name_restaurant;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    //getters
    public String getId() {
        return id;
    }

    public AppUser getOwner() {
        return owner;
    }

    public String getName_restaurant() {
        return name_restaurant;
    }

    public String getDirection() {
        return direction;
    }

    public String getPhone() {
        return phone;
    }

    public Type getType() {
        return type;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
