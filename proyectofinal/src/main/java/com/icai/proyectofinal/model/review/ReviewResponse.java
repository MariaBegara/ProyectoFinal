package com.icai.proyectofinal.model.review;

public record ReviewResponse(
        String id,
        String content,
        float score,
        String userId,
        String userName,
        String restaurantId,
        String restaurantName
) {
}
