package com.icai.proyectofinal.service;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppReview;
import com.icai.proyectofinal.model.Type;
import com.icai.proyectofinal.repository.RestaurantRepository;
import com.icai.proyectofinal.repository.ReviewRepository;
import com.icai.proyectofinal.service.restaurant.RestaurantService;
import com.icai.proyectofinal.model.restaurant.RestaurantRegister;
import com.icai.proyectofinal.model.restaurant.RestaurantResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRestaurants_returnsList() {
        List<AppRestaurant> list = List.of(new AppRestaurant());
        when(restaurantRepository.findAll()).thenReturn(list);
        assertEquals(list, restaurantService.getAllRestaurants());
    }

    @Test
    void saveRestaurant_saves() {
        AppRestaurant r = new AppRestaurant();
        restaurantService.saveRestaurant(r);
        verify(restaurantRepository).save(r);
    }

    @Test
    void saveRestaurant_fromRegister_savesAndReturnsResponse() {
        RestaurantRegister register = new RestaurantRegister(
                "Restaurante Test",
                "123456789",
                Type.FUSION,
                "Calle 1",
                "40.0",
                "-3.0",
                "ejemplo@ejemplo.com",
                "1234"
        );
        AppRestaurant saved = new AppRestaurant();
        saved.setId("1");
        saved.setName_restaurant("Restaurante Test");
        saved.setPhone("123456789");
        saved.setDirection("Calle 1");
        saved.setType(Type.FUSION);
        saved.setLatitude("40.0");
        saved.setLongitude("-3.0");
        when(restaurantRepository.save(any(AppRestaurant.class))).thenReturn(saved);
        RestaurantResponse response = restaurantService.saveRestaurant(register);
        assertEquals("Restaurante Test", response.name_restaurant());
        assertEquals("123456789", response.phone());
        assertEquals("Calle 1", response.direction());
        assertEquals("FUSION", response.type());
    }

    @Test
    void getRestaurantsFiltered_invalidType_throws() {
        assertThrows(ResponseStatusException.class, () -> restaurantService.getRestaurantsFiltered("INVALID", null));
    }

    @Test
    void getRestaurantsFiltered_noType_returnsAll() throws Exception {
        AppRestaurant r = new AppRestaurant();
        java.lang.reflect.Field typeField = AppRestaurant.class.getDeclaredField("type");
        typeField.setAccessible(true);
        typeField.set(r, Type.FUSION);
        java.lang.reflect.Field idField = AppRestaurant.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(r, "1");
        java.lang.reflect.Field nameField = AppRestaurant.class.getDeclaredField("name_restaurant");
        nameField.setAccessible(true);
        nameField.set(r, "Test");
        java.lang.reflect.Field latField = AppRestaurant.class.getDeclaredField("latitude");
        latField.setAccessible(true);
        latField.set(r, "1");
        java.lang.reflect.Field lonField = AppRestaurant.class.getDeclaredField("longitude");
        lonField.setAccessible(true);
        lonField.set(r, "2");
        java.lang.reflect.Field dirField = AppRestaurant.class.getDeclaredField("direction");
        dirField.setAccessible(true);
        dirField.set(r, "dir");
        java.lang.reflect.Field phoneField = AppRestaurant.class.getDeclaredField("phone");
        phoneField.setAccessible(true);
        phoneField.set(r, "123");
        when(restaurantRepository.findAll()).thenReturn(List.of(r));
        when(reviewRepository.findByRestaurant(r)).thenReturn(Collections.emptyList());
        List<RestaurantResponse> result = restaurantService.getRestaurantsFiltered(null, null);
        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).name_restaurant());
    }

    @Test
    void getRestaurantsFiltered_withMinScore_filters() throws Exception {
        AppRestaurant r = new AppRestaurant();
        java.lang.reflect.Field typeField = AppRestaurant.class.getDeclaredField("type");
        typeField.setAccessible(true);
        typeField.set(r, Type.FUSION);
        java.lang.reflect.Field idField = AppRestaurant.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(r, "1");
        java.lang.reflect.Field nameField = AppRestaurant.class.getDeclaredField("name_restaurant");
        nameField.setAccessible(true);
        nameField.set(r, "Test");
        java.lang.reflect.Field latField = AppRestaurant.class.getDeclaredField("latitude");
        latField.setAccessible(true);
        latField.set(r, "1");
        java.lang.reflect.Field lonField = AppRestaurant.class.getDeclaredField("longitude");
        lonField.setAccessible(true);
        lonField.set(r, "2");
        java.lang.reflect.Field dirField = AppRestaurant.class.getDeclaredField("direction");
        dirField.setAccessible(true);
        dirField.set(r, "dir");
        java.lang.reflect.Field phoneField = AppRestaurant.class.getDeclaredField("phone");
        phoneField.setAccessible(true);
        phoneField.set(r, "123");
        AppReview review = new AppReview();
        java.lang.reflect.Field scoreField = AppReview.class.getDeclaredField("score");
        scoreField.setAccessible(true);
        scoreField.set(review, 5);
        when(restaurantRepository.findAll()).thenReturn(List.of(r));
        when(reviewRepository.findByRestaurant(r)).thenReturn(List.of(review));
        List<RestaurantResponse> result = restaurantService.getRestaurantsFiltered(null, 4.0);
        assertEquals(1, result.size());
        assertTrue(result.get(0).averageScore() >= 4.0);
    }
}
