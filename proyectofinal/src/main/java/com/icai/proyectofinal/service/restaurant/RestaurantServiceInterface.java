package com.icai.proyectofinal.service.restaurant;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.restaurant.RestaurantRegister;
import com.icai.proyectofinal.model.restaurant.RestaurantResponse;

import java.util.List;

public interface RestaurantServiceInterface {
    void saveRestaurant(AppRestaurant restaurant);
    RestaurantResponse saveRestaurant(RestaurantRegister register, AppUser owner);
    RestaurantResponse saveRestaurant(RestaurantRegister register);
    List<AppRestaurant> getAllRestaurants();
    List<RestaurantResponse> getRestaurants(AppUser user);
    List<RestaurantResponse> getRestaurantsFiltered(String type, Double minScore);
    /*
    RestaurantResponse saveRestaurant (RestaurantRegister register);
    List<AppRestaurant> getAllRestaurants ();
    void saveRestaurant(AppRestaurant restaurant);
    RestaurantResponse saveRestaurant(RestaurantRegister register, AppUser owner);
    //List<AppRestaurant> getRestaurantsByType (String type);
    List<RestaurantResponse> getRestaurantsFiltered(String type, Double minScore);
    List<RestaurantResponse> getRestaurants(AppUser user);

     */
}
