package com.icai.proyectofinal.controllers;

import com.icai.proyectofinal.model.restaurant.RestaurantRegister;
import com.icai.proyectofinal.model.restaurant.RestaurantResponse;
import com.icai.proyectofinal.service.restaurant.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/mostrar/todos")
    public List<RestaurantResponse> getAll() {
        return restaurantService.getRestaurantsFiltered(null, null);
    }

    /*
    /restaurantes/filtrar?tipo=FUSION&minScore=4.0
    */
    @GetMapping("/filtrar")
    public List<RestaurantResponse> getFiltered(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Double minScore) {
        return restaurantService.getRestaurantsFiltered(tipo, minScore);
    }

    @PostMapping("/nuevo")
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse register(
            @Valid
            @RequestBody RestaurantRegister register) {
        try {
            return restaurantService.saveRestaurant(register);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

}
