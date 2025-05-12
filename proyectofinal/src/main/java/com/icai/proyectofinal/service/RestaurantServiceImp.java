package com.icai.proyectofinal.service;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImp implements RestaurantService{
    @Autowired
    private RestaurantRepository restaurantRepository;

    public RestaurantServiceImp(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<AppRestaurant> getAllRestaurants() {
        return (List<AppRestaurant>) restaurantRepository.findAll();
    }
    @Override
    public void saveRestaurant(AppRestaurant restaurant) {
        restaurantRepository.save(restaurant);
    }
    //luego añadiremos aqui los filtros para las busquedas


}
