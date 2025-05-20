package com.icai.proyectofinal.controllers;

import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.restaurant.RestaurantRegister;
import com.icai.proyectofinal.model.restaurant.RestaurantResponse;
import com.icai.proyectofinal.service.restaurant.RestaurantService;
import com.icai.proyectofinal.service.user.UserService;
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
    @Autowired
    private UserService userService;

    @GetMapping("/mostrar/todos")
    public List<RestaurantResponse> getAll() {
        return restaurantService.getRestaurantsFiltered(null, null);
    }

    @GetMapping("/filtrar")
    public List<RestaurantResponse> getFiltered(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Double minScore) {
        return restaurantService.getRestaurantsFiltered(tipo, minScore);
    }

    //@PreAuthorize("hasRole('OWNER')")
    @GetMapping("/yo")
    public List<RestaurantResponse> getMyRestaurants(
            @CookieValue("session") String sessionId) {
        System.out.println("cargando restaurantes");
        AppUser user = userService.authenticate(sessionId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return restaurantService.getRestaurants(user);
    }

    //@PreAuthorize("hasRole('OWNER')")
    @PostMapping("/nuevo")
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse register(

            @CookieValue("session") String sessionId,
            @Valid
            @RequestBody RestaurantRegister register) {
        try {

            System.out.println("📥 Petición recibida para crear restaurante");
            AppUser owner = userService.authenticate(sessionId);
            if (owner == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            return restaurantService.saveRestaurant(register, owner);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

}
