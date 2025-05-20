package com.icai.proyectofinal.controllers;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.restaurant.RestaurantResponse;
import com.icai.proyectofinal.service.restaurant.RestaurantService;
import com.icai.proyectofinal.service.review.ReviewService;
import com.icai.proyectofinal.model.review.ReviewRegister;
import com.icai.proyectofinal.model.review.ReviewResponse;
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

    //@PreAuthorize("hasRole('USER')")
    @PostMapping("/nuevo")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse register(
            @Valid
            @RequestBody ReviewRegister register,
            @RequestBody AppUser usuario,
            @RequestBody AppRestaurant restaurante) {
        try {
            return reviewService.saveReview(register, usuario, restaurante);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @GetMapping("/filtrar/usuario")
    public List<ReviewResponse> getFiltered(
            @RequestParam AppUser usuario) {
        try {
            return reviewService.getReviewsUser(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/filtrar/restaurante")
    public List<ReviewResponse> getFiltered(
            @RequestParam AppRestaurant restaurant) {
        try {
            return reviewService.getReviewsRestaurant(restaurant);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


}
