package com.icai.proyectofinal.controllers;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.model.RestaurantResponse;
import com.icai.proyectofinal.service.RestaurantInterface;
import com.icai.proyectofinal.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/mostrarlista")
    public List<RestaurantResponse> getAll() {
        return restaurantService.getRestaurantsFiltered(null, null);
    }

    @GetMapping("/filtrar")
    public List<RestaurantResponse> getFiltered(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Double minScore) {
        return restaurantService.getRestaurantsFiltered(tipo, minScore);
    }

    /*
    /restaurantes/filtrar?tipo=FUSION&minScore=4.0
     */



}
