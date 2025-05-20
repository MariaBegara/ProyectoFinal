package com.icai.proyectofinal.repository;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<AppReview, String> {
    List<AppReview> findByRestaurant(AppRestaurant restaurant);
    List<AppReview> findByUser (String user_name);
    Optional<AppReview> findById (String user_id);
}
