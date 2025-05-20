package com.icai.proyectofinal.controllers;

import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.restaurant.RestaurantRegister;
import com.icai.proyectofinal.model.restaurant.RestaurantResponse;
import com.icai.proyectofinal.service.restaurant.RestaurantService;
import com.icai.proyectofinal.service.user.UserService;
import com.icai.proyectofinal.entity.Token;
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
/*
    //@PreAuthorize("hasRole('OWNER')")
    @PostMapping("/nuevo")
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse register(
            @CookieValue("session") String sessionId,
            @Valid
            @RequestBody RestaurantRegister register) {
        try {

            (register.email(), register.password());
            AppUser user = userService.authenticate(token.getId());

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            return restaurantService.saveRestaurant(register, user);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }
}
*/
/*
    @PostMapping("/nuevo")
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse register(
            @CookieValue("session") String sessionId,
            @Valid @RequestBody RestaurantRegister register) {

        System.out.println("📥 Petición recibida para crear restaurante con sesión: " + sessionId);

        AppUser owner = userService.authenticate(sessionId);
        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión no válida");
        }

        return restaurantService.saveRestaurant(register, owner);
    }
*/

    @PostMapping("/nuevo")
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse register(
            @CookieValue("session") String sessionId,
            @Valid @RequestBody RestaurantRegister register) {
        try {
            // Verificar si el usuario está autenticado
            AppUser user = userService.authenticate(sessionId);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado.");
            }

            // Intentar registrar el restaurante
            return restaurantService.saveRestaurant(register, user);
        } catch (DataIntegrityViolationException e) {
            // Ocurrió un conflicto en la base de datos (ej. datos duplicados)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El restaurante ya existe o hay datos inválidos.", e);
        } catch (Exception e) {
            // Manejo genérico de errores
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar el restaurante.", e);
        }
    }



}


