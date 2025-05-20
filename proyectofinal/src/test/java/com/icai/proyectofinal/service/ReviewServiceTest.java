package com.icai.proyectofinal.service;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppReview;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.review.ReviewRegister;
import com.icai.proyectofinal.model.review.ReviewResponse;
import com.icai.proyectofinal.repository.RestaurantRepository;
import com.icai.proyectofinal.repository.ReviewRepository;
import com.icai.proyectofinal.repository.UserRepository;
import com.icai.proyectofinal.service.review.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveReview_ok() {
        AppUser user = new AppUser();
        user.setId("u1");
        AppRestaurant rest = new AppRestaurant();
        rest.setId("r1");
        ReviewRegister reg = new ReviewRegister("Muy bueno", 5);
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(restaurantRepository.findById("r1")).thenReturn(Optional.of(rest));
        when(reviewRepository.save(any(AppReview.class))).thenAnswer(inv -> inv.getArgument(0));
        ReviewResponse resp = reviewService.saveReview(reg, user, rest);
        assertEquals("Muy bueno", resp.content());
        assertEquals(5, resp.score());
    }

    @Test
    void saveReview_usuarioNoExiste_lanzaExcepcion() {
        AppUser user = new AppUser();
        user.setId("u2");
        AppRestaurant rest = new AppRestaurant();
        rest.setId("r2");
        ReviewRegister reg = new ReviewRegister("Malo", 2);
        when(userRepository.findById("u2")).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> reviewService.saveReview(reg, user, rest));
    }

    @Test
    void saveReview_restauranteNoExiste_lanzaExcepcion() {
        AppUser user = new AppUser();
        user.setId("u3");
        AppRestaurant rest = new AppRestaurant();
        rest.setId("r3");
        ReviewRegister reg = new ReviewRegister("Regular", 3);
        when(userRepository.findById("u3")).thenReturn(Optional.of(user));
        when(restaurantRepository.findById("r3")).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> reviewService.saveReview(reg, user, rest));
    }

    @Test
    void getReviewsUser_devuelveLista() {
        AppUser user = new AppUser();
        user.setId("u4");
        user.setName("Pepe");
        AppRestaurant rest = new AppRestaurant();
        rest.setId("r4");
        rest.setName_restaurant("Restaurante 4");
        AppReview review = new AppReview();
        review.setContent("Genial");
        review.setScore(4);
        review.setUser(user);
        review.setRestaurant(rest);
        when(reviewRepository.findByUser_Id("u4")).thenReturn(List.of(review));
        List<ReviewResponse> result = reviewService.getReviewsUser(user);
        assertEquals(1, result.size());
        assertEquals("Genial", result.get(0).content());
    }

    @Test
    void getReviewsRestaurant_devuelveLista() {
        AppRestaurant rest = new AppRestaurant();
        rest.setId("r5");
        rest.setName_restaurant("Restaurante 5");
        AppUser user = new AppUser();
        user.setId("u5");
        user.setName("Ana");
        AppReview review = new AppReview();
        review.setContent("Bueno");
        review.setScore(5);
        review.setUser(user);
        review.setRestaurant(rest);
        when(restaurantRepository.findById("r5")).thenReturn(Optional.of(rest));
        when(reviewRepository.findByRestaurant(rest)).thenReturn(List.of(review));
        List<ReviewResponse> result = reviewService.getReviewsRestaurant(rest);
        assertEquals(1, result.size());
        assertEquals("Bueno", result.get(0).content());
    }
}
