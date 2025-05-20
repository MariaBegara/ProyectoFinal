package com.icai.proyectofinal.service.restaurant;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppReview;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.*;
import com.icai.proyectofinal.model.restaurant.RestaurantRegister;
import com.icai.proyectofinal.model.restaurant.RestaurantResponse;
import com.icai.proyectofinal.repository.RestaurantRepository;
import com.icai.proyectofinal.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RestaurantService implements RestaurantServiceInterface {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void saveRestaurant(AppRestaurant restaurant) {
        restaurantRepository.save(restaurant);
    }
    //luego añadiremos aquí los filtros para las busquedas

    public RestaurantResponse saveRestaurant(RestaurantRegister register, AppUser owner) {
        AppRestaurant restaurant = new AppRestaurant();
        restaurant.setOwner(owner);
        restaurant.setName_restaurant(register.name_restaurant());
        restaurant.setPhone(register.phone());
        restaurant.setDirection(register.direction());
        restaurant.setType(register.tipo());
        restaurant.setLatitude(register.latitude());
        restaurant.setLongitude(register.longitude());

        // Usar el objeto devuelto por save para obtener el id y otros datos
        AppRestaurant saved = restaurantRepository.save(restaurant);
        double avg = 0.0;
        return new RestaurantResponse(
                saved.getId(),
                saved.getName_restaurant(),
                saved.getLatitude(),
                saved.getLongitude(),
                saved.getDirection(),
                saved.getPhone(),
                saved.getType().toString(),
                avg
        );
    }

    @Override
    public RestaurantResponse saveRestaurant(RestaurantRegister register) {
        // Permitir test unitario: crear restaurante sin owner
        AppRestaurant restaurant = new AppRestaurant();
        restaurant.setName_restaurant(register.name_restaurant());
        restaurant.setPhone(register.phone());
        restaurant.setDirection(register.direction());
        restaurant.setType(register.tipo());
        restaurant.setLatitude(register.latitude());
        restaurant.setLongitude(register.longitude());
        AppRestaurant saved = restaurantRepository.save(restaurant);
        double avg = 0.0;
        return new RestaurantResponse(
                saved.getId(),
                saved.getName_restaurant(),
                saved.getLatitude(),
                saved.getLongitude(),
                saved.getDirection(),
                saved.getPhone(),
                saved.getType().toString(),
                avg
        );
    }

    @Override
    public List<AppRestaurant> getAllRestaurants() {
        return (List<AppRestaurant>) restaurantRepository.findAll();
    }

    @Override
    public List<RestaurantResponse> getRestaurantsFiltered(String type, Double minScore) {
        List<AppRestaurant> restaurantes;

        if (type != null) {
            try {
                restaurantes = restaurantRepository.findByType(Type.valueOf(type.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo no válido");
            }
        } else {
            restaurantes = restaurantRepository.findAll();
        }

        return restaurantes.stream()
                .map(r -> {
                    List<AppReview> reviews = reviewRepository.findByRestaurant(r);
                    double avg;
                    if (reviews.isEmpty()) {
                        avg = 0.0;
                    } else {
                        double sum = 0.0;
                        for (AppReview rev : reviews) {
                            sum += rev.getScore();
                        }
                        avg = sum / reviews.size();
                    }
                    return new RestaurantResponse(
                            r.getId(),
                            r.getName_restaurant(),
                            r.getLatitude(),
                            r.getLongitude(),
                            r.getDirection(),
                            r.getPhone(),
                            r.getType().toString(),
                            avg
                    );
                })
                .filter(resp -> minScore == null || resp.averageScore() >= minScore)
                .toList();
    }

}
