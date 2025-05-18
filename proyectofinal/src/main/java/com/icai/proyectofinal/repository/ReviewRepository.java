package com.icai.proyectofinal.repository;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<AppReview, String> {
    List<AppReview> findByRestaurant(AppRestaurant restaurant);

}
