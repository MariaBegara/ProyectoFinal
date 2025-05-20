package com.icai.proyectofinal.service.review;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.review.ReviewRegister;
import com.icai.proyectofinal.model.review.ReviewResponse;
import com.icai.proyectofinal.model.review.ReviewRequest;

import java.util.List;

public interface ReviewServiceInterface {
    ReviewResponse saveReview (ReviewRegister register, AppUser usuario, AppRestaurant restaurante);
    List<ReviewResponse> getReviewsUser (AppUser appUser);
    List<ReviewResponse> getReviewsRestaurant (AppRestaurant appRestaurant);
    //ReviewResponse getReview (ReviewRegister register, AppUser appUser);
    ReviewResponse updateReview (ReviewRequest register);
}