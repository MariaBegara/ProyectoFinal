package com.icai.proyectofinal.service;

import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.entity.Token;
import com.icai.proyectofinal.model.user.ProfileRequest;
import com.icai.proyectofinal.model.user.ProfileResponse;
import com.icai.proyectofinal.model.user.RegisterRequest;

public interface UserServiceInterface {

    Token login(String email, String password);
    AppUser authenticate(String tokenId);
    ProfileResponse profile(AppUser appUser);
    ProfileResponse profile(RegisterRequest register);
    ProfileResponse updateProfile(AppUser appUser, ProfileRequest profile);
    ProfileResponse register(RegisterRequest register);
    void logout(String tokenId);
    void delete(AppUser appUser);

}