package com.icai.proyectofinal.service;

import com.icai.proyectofinal.entity.AppRestaurant;

import java.util.List;

public interface RestaurantService {
    List<AppRestaurant> getAllRestaurants();
    void saveRestaurant(AppRestaurant restaurant);
}
