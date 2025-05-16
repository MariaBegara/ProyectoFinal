
package com.icai.proyectofinal.controllers;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/mostrarlista")
    public List<AppRestaurant> getAll() {
        return restaurantService.getAllRestaurants();
    }




}
