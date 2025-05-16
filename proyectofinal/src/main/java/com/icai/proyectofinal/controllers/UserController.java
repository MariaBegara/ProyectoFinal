package com.icai.proyectofinal.controllers;

import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.errors.RecursoNoEncontradoException;
import com.icai.proyectofinal.model.ProfileResponse;
import com.icai.proyectofinal.model.RegisterRequest;
import com.icai.proyectofinal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/usuario")
public class UserController {
    @Autowired
    private UserService userService;

    //@PreAuthorize("hasRole('USER')")

    @PostMapping("/nuevo")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(
            @Valid
            @RequestBody RegisterRequest register) {
        try {
            return userService.profile(register);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @GetMapping("/usuario/yo")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse profile(
            @CookieValue(value = "session", required = true) String session) {
        AppUser appUser = userService.authenticate(session);
        if (appUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return userService.profile(appUser);
    }



}
