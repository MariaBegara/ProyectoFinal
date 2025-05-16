package com.icai.proyectofinal.repository;

import com.icai.proyectofinal.entity.AppReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<AppReview, String> {

}
