package com.icai.proyectofinal.service;

import com.icai.proyectofinal.entity.AppRestaurant;

import java.util.List;

public interface RestaurantInterface {
    List<AppRestaurant> getAllRestaurants();
    void saveRestaurant(AppRestaurant restaurant);
    List<AppRestaurant> getRestaurantsByType(String type);
}
