package com.icai.proyectofinal.repository;

import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
    Token findByAppUser(Optional<AppUser> appUser); // consultar el Token asociado a un AppUser dado

}
