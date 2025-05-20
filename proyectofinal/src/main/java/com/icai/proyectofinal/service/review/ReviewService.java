package com.icai.proyectofinal.service.review;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppReview;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.review.ReviewRegister;
import com.icai.proyectofinal.model.review.ReviewRequest;
import com.icai.proyectofinal.model.review.ReviewResponse;
import com.icai.proyectofinal.repository.RestaurantRepository;
import com.icai.proyectofinal.repository.ReviewRepository;
import com.icai.proyectofinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReviewService implements ReviewServiceInterface {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewResponse saveReview (ReviewRegister register, AppUser usuario, AppRestaurant restaurante){

        // Buscar usuario:
        Optional<AppUser> optionalUser = userRepository.findById(usuario.getId());
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        AppUser user = optionalUser.get();

        // Buscar restaurante:
        Optional<AppRestaurant> optionalRestaurant = restaurantRepository.findById(restaurante.getId());

        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante no encontrado");
        }
        AppRestaurant restaurant = optionalRestaurant.get();


        AppReview review = new AppReview();
        review.setContent(register.content());
        review.setScore(register.score());
        review.setUser(user);
        review.setRestaurant(restaurant);

        reviewRepository.save(review);

        return new ReviewResponse(
                review.getId(),
                review.getContent(),
                review.getScore(),
                user.getId(),
                user.getName(),
                restaurant.getId(),
                restaurant.getName_restaurant()
        );
    }


    @Override
    public List<ReviewResponse> getReviewsUser(AppUser appUser) {
        List<AppReview> reviews = reviewRepository.findByUser(appUser.getId());

        List<ReviewResponse> responses = new ArrayList<>();

        reviews.forEach(review -> {
            AppRestaurant restaurant = review.getRestaurant();

            ReviewResponse response = new ReviewResponse(
                    review.getId(),
                    review.getContent(),
                    review.getScore(),
                    appUser.getId(),
                    appUser.getName(),
                    restaurant.getId(),
                    restaurant.getName_restaurant()
            );

            responses.add(response);
        });

        return responses;
    }


    @Override
    public List<ReviewResponse> getReviewsRestaurant(AppRestaurant appRestaurant) {
        Optional<AppRestaurant> optionalRestaurant = restaurantRepository.findById(appRestaurant.getId());

        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante no encontrado");
        }

        AppRestaurant restaurant = optionalRestaurant.get();

        List<AppReview> reviews = reviewRepository.findByRestaurant(restaurant);

        List<ReviewResponse> responses = new ArrayList<>();

        reviews.forEach(review -> {
            AppUser user = review.getUser();

            ReviewResponse response = new ReviewResponse(
                    review.getId(),
                    review.getContent(),
                    review.getScore(),
                    user.getId(),
                    user.getName(),
                    restaurant.getId(),
                    restaurant.getName_restaurant()
            );

            responses.add(response);
        });

        return responses;
    }


    @Override
    public ReviewResponse updateReview(ReviewRequest request) {
        AppReview review = reviewRepository.findById(request.reviewId())
                .orElseThrow(() -> new NoSuchElementException("Reseña no encontrada"));

        // Actualizar contenido y score
        review.setContent(request.content());
        review.setScore(request.score());

        reviewRepository.save(review);

        AppUser user = review.getUser();
        AppRestaurant restaurant = review.getRestaurant();

        return new ReviewResponse(
                review.getId(),
                review.getContent(),
                review.getScore(),
                user.getId(),
                user.getName(),
                restaurant.getId(),
                restaurant.getName_restaurant()
        );
    }


}







