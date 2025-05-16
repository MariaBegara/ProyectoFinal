package com.icai.proyectofinal.repository;

import com.icai.proyectofinal.entity.AppRestaurant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends CrudRepository<AppRestaurant,String> {
    //No es necesario porque ya lo tiene pero lo pongo
    List<AppRestaurant> findAll();

}