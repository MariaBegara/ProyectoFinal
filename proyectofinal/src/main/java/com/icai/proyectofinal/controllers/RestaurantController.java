package com.icai.proyectofinal.controllers;

import com.icai.proyectofinal.model.restaurant.RestaurantRegister;
import com.icai.proyectofinal.model.restaurant.RestaurantResponse;
import com.icai.proyectofinal.service.restaurant.RestaurantService;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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
            @RequestBody RestaurantRegister register,
            @CookieValue(value = "session", required = false) String session
    ) {
        if (session == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        AppUser owner = userService.authenticate(session);
        if (owner == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        try {
            return restaurantService.saveRestaurant(register, owner);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

}
