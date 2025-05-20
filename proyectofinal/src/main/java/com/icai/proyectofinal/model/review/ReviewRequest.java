package com.icai.proyectofinal.model.review;

import com.icai.proyectofinal.model.Role;

public record ReviewRequest(
        String reviewId,
        String userId,
        String restaurantId,
        Role role,
        String content,
        float score
) {
}
