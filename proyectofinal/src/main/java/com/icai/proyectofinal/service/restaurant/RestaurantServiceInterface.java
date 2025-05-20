package com.icai.proyectofinal.service.restaurant;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.model.restaurant.RestaurantRegister;
import com.icai.proyectofinal.model.restaurant.RestaurantResponse;

import java.util.List;

public interface RestaurantServiceInterface {
    RestaurantResponse saveRestaurant (RestaurantRegister register);
    List<AppRestaurant> getAllRestaurants ();
    void saveRestaurant(AppRestaurant restaurant);
    //List<AppRestaurant> getRestaurantsByType (String type);
    List<RestaurantResponse> getRestaurantsFiltered(String type, Double minScore);
}
