package com.icai.proyectofinal.controllers;

import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.entity.Token;
import com.icai.proyectofinal.model.user.ProfileRequest;
import com.icai.proyectofinal.model.user.ProfileResponse;
import com.icai.proyectofinal.model.user.RegisterRequest;
import com.icai.proyectofinal.model.user.LoginRequest;
import com.icai.proyectofinal.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseCookie;
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

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid
            @RequestBody LoginRequest credentials) {
        Token token = userService.login(credentials.email(), credentials.password());
        if (token == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // Credenciales incorrectas
        ResponseCookie session = ResponseCookie
                .from("session", token.id)
                .httpOnly(true)
                .path("/")
                .sameSite("Strict")
                .build();
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, session.toString()).build();
    }

    @GetMapping("/yo")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse profile(
            @CookieValue(value = "session", required = false) String session) {
        AppUser appUser = userService.authenticate(session);
        if (appUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return userService.profile(appUser);
    }

    @PutMapping("/yo")
    public ProfileResponse updateProfile(
            @CookieValue(value = "session") String session,
            @RequestBody ProfileRequest profile) {
        AppUser appUser = userService.authenticate(session);
        if (appUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return userService.updateProfile(appUser, profile);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
            @CookieValue(value = "session") String session) {
        userService.logout(session);
    }

    @DeleteMapping("/yo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @CookieValue(value = "session") String session) {
        AppUser appUser = userService.authenticate(session);
        if (appUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        userService.delete(appUser);
    }

}
