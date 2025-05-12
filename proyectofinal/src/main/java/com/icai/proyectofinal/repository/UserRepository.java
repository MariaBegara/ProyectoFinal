package com.icai.proyectofinal.repository;


import com.icai.proyectofinal.entity.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<AppUser, String> {
    Optional<AppUser> findByEmail(String email);

}
