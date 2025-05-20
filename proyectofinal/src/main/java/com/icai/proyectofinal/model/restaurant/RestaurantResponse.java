package com.icai.proyectofinal.model.restaurant;

import com.icai.proyectofinal.model.Type;

public record RestaurantResponse(
        String id,
        String name_restaurant,
        String latitude,
        String longitude,
        String direction,
        String phone,
        String type,
        double averageScore
) {}
