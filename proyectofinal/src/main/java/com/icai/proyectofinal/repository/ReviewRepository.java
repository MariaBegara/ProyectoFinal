package com.icai.proyectofinal.repository;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppReview;
import com.icai.proyectofinal.entity.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<AppReview, String> {
    List<AppReview> findByRestaurant(AppRestaurant restaurant);
    List<AppReview> findByUser_Id(String userId);
    //List<AppReview> findByUser (String user_name); -> no es valido
    //List<AppReview> findByUser (AppUser user_name);
    //Optional<AppReview> findById (String userId);

}
