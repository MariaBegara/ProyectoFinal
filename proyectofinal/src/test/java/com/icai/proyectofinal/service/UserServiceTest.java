package com.icai.proyectofinal.service;

import com.icai.proyectofinal.Hashing;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.entity.Token;
import com.icai.proyectofinal.model.user.ProfileRequest;
import com.icai.proyectofinal.model.user.ProfileResponse;
import com.icai.proyectofinal.model.user.RegisterRequest;
import com.icai.proyectofinal.repository.TokenRepository;
import com.icai.proyectofinal.repository.UserRepository;
import com.icai.proyectofinal.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private Hashing hashing;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ok() {
        AppUser user = new AppUser();
        user.password = "hashed";
        when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));
        when(hashing.compare("hashed", "pass")).thenReturn(true);
        Token token = new Token();
        when(tokenRepository.findByAppUser(any())).thenReturn(token);
        assertEquals(token, userService.login("mail", "pass"));
    }

    @Test
    void login_fail_wrongPassword() {
        AppUser user = new AppUser();
        user.password = "hashed";
        when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));
        when(hashing.compare("hashed", "pass")).thenReturn(false);
        assertNull(userService.login("mail", "pass"));
    }

    @Test
    void authenticate_returnsUser() {
        Token token = new Token();
        AppUser user = new AppUser();
        token.appUser = user;
        when(tokenRepository.findById("tid")).thenReturn(Optional.of(token));
        assertEquals(user, userService.authenticate("tid"));
    }

    @Test
    void authenticate_nullIfNotFound() {
        when(tokenRepository.findById("tid")).thenReturn(Optional.empty());
        assertNull(userService.authenticate("tid"));
    }

    @Test
    void profile_fromAppUser() {
        AppUser user = new AppUser();
        user.email = "e";
        user.name = "n";
        user.role = "r";
        ProfileResponse resp = userService.profile(user);
        assertEquals("e", resp.email());
        assertEquals("n", resp.nombre());
        assertEquals("r", resp.role());
    }

    @Test
    void profile_fromRegisterRequest_passwordMismatch() {
        RegisterRequest req = new RegisterRequest("u", "e", "p1", "p2", "n", "r");
        assertThrows(IllegalArgumentException.class, () -> userService.profile(req));
    }

    @Test
    void updateProfile_changesNameAndPassword() {
        AppUser user = new AppUser();
        ProfileRequest req = new ProfileRequest("nuevo", null, "claveNueva1");
        when(hashing.hash("claveNueva1")).thenReturn("hashed");
        ProfileResponse resp = userService.updateProfile(user, req);
        assertEquals("nuevo", user.name);
        assertEquals("hashed", user.password);
        assertEquals("nuevo", resp.nombre());
    }

    @Test
    void register_passwordMismatch_throws() {
        RegisterRequest req = new RegisterRequest("u", "e", "p1", "p2", "n", "r");
        assertThrows(IllegalArgumentException.class, () -> userService.register(req));
    }

    @Test
    void logout_deletesToken() {
        userService.logout("tid");
        verify(tokenRepository).deleteById("tid");
    }

    @Test
    void delete_deletesUser() {
        AppUser user = new AppUser();
        userService.delete(user);
        verify(userRepository).delete(user);
    }
}

