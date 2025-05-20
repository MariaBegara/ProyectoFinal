package com.icai.proyectofinal.controllers;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.restaurant.RestaurantResponse;
import com.icai.proyectofinal.model.review.ReviewRequest;
import com.icai.proyectofinal.model.user.ProfileRequest;
import com.icai.proyectofinal.model.user.ProfileResponse;
import com.icai.proyectofinal.service.restaurant.RestaurantService;
import com.icai.proyectofinal.service.review.ReviewService;
import com.icai.proyectofinal.model.review.ReviewRegister;
import com.icai.proyectofinal.model.review.ReviewResponse;
import com.icai.proyectofinal.repository.RestaurantRepository;
import com.icai.proyectofinal.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserService userService;

    //@PreAuthorize("hasRole('USER')")
    @PostMapping("/nuevo")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse register(
            @Valid
            @RequestBody ReviewRegister register,
            @CookieValue(value = "session", required = false) String session,
            @RequestParam(value = "restauranteId", required = false) String restauranteId) {
        if (session == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        if (restauranteId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta restauranteId");
        AppUser usuario = userService.authenticate(session);
        if (usuario == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        AppRestaurant restaurante = restaurantRepository.findById(restauranteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante no encontrado"));
        try {
            return reviewService.saveReview(register, usuario, restaurante);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @GetMapping("/filtrar/usuario")
    public List<ReviewResponse> getFiltered(
            @CookieValue(value = "session", required = false) String session) {
        if (session == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        AppUser usuario = userService.authenticate(session);
        if (usuario == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        try {
            return reviewService.getReviewsUser(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/filtrar/usuario")
    public ReviewResponse updateReview(
            @RequestBody ReviewRequest review) {
        try {
            return reviewService.updateReview(review);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @GetMapping("/filtrar/restaurante")
    public List<ReviewResponse> getFiltered(
            @RequestBody AppRestaurant restaurant) {
        try {
            return reviewService.getReviewsRestaurant(restaurant);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


}
