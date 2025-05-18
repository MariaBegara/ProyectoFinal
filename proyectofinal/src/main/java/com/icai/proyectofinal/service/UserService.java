package com.icai.proyectofinal.service;

import com.icai.proyectofinal.Hashing;
import com.icai.proyectofinal.entity.Token;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.ProfileRequest;
import com.icai.proyectofinal.model.ProfileResponse;
import com.icai.proyectofinal.model.RegisterRequest;
import com.icai.proyectofinal.repository.TokenRepository;
import com.icai.proyectofinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService implements UserServiceInterface{

    @Autowired
    private UserRepository userRepository; // es necesario para poder utilizar los métodos de AppUserRepository
    @Autowired
    private TokenRepository tokenRepository; // es necesario para poder utilizar los métodos de TokenRepository
    @Autowired
    private Hashing hashing;
    /**
     * @param email email proporcionado para el login
     * @param password password proporcionado para el login
     * @return si las credenciales del usuario son correctas, retorna un token de sesión asociado a dicho usuario;
     * si las credenciales son incorrectas, retorna null
     */
    @Override
    public Token login(String email, String password) {
        Optional<AppUser> appUserOpt = userRepository.findByEmail(email);
        if (appUserOpt.isEmpty()) return null;

        AppUser appUser = appUserOpt.get();
        if (!hashing.compare(appUser.password, password)) return null;

        Token token = tokenRepository.findByAppUser(appUserOpt);
        if (token != null) return token;

        Token nuevo = new Token();
        nuevo.appUser = appUser;
        tokenRepository.save(nuevo);
        return nuevo;
    }



    /**
     * @param tokenId token de la sesión actual del usuario
     * @return si la sesión está creada (el token existe en BD), retorna el usuario asociado a dicha sesión;
     * si la sesión no existe, retorna null
     */
    @Override
    public AppUser authenticate(String tokenId) {
        Optional<Token> tokenOpt = tokenRepository.findById(tokenId);
        return tokenOpt.map(token -> token.appUser).orElse(null);
    }



    /**
     * @param appUser usuario
     * @return respuesta con el perfil de dicho usuario
     */
    @Override
    public ProfileResponse profile(AppUser appUser) {
        return new ProfileResponse(appUser.email, appUser.name, appUser.role);
    }



    /**
     * Registra un nuevo usuario a partir de RegisterRequest.
     */
    @Override
    public ProfileResponse profile(RegisterRequest register) {
        if (!register.password1().equals(register.password2())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        AppUser user = new AppUser();
        user.name = register.name();
        user.email = register.email();
        user.password = hashing.hash(register.password1());
        user.role = register.role();

        userRepository.save(user);

        return profile(user); // reutiliza el método profile(AppUser)
    }



    /**
     * @param appUser usuario
     * @param profile nuevos datos para el perfil del usuario
     * @return respuesta con el perfil de dicho usuario actualizado
     */
    @Override
    public ProfileResponse updateProfile(AppUser appUser, ProfileRequest profile) {
        if (profile.name() != null && !profile.name().isEmpty()) {
            appUser.name = profile.name();
        }

        if (profile.password() != null && !profile.password().isEmpty()) {
            appUser.password = hashing.hash(profile.password());
        }

        userRepository.save(appUser);
        return profile(appUser);
    }


    /**
     *
     * @param register datos de registro del usuario
     * @return respuesta con el perfil del nuevo usuario:
     */
    @Override
    public ProfileResponse register(RegisterRequest register) {
        if (!register.password1().equals(register.password2())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        AppUser usuario = new AppUser();
        usuario.setName_user(register.name_user()); // <- ¡esto es obligatorio!
        usuario.setEmail(register.email());
        usuario.setPassword(hashing.hash(register.password1()));
        usuario.setName(register.name());
        usuario.setRole(register.role());
        userRepository.save(usuario);

        return profile(usuario);
    }


    /**
     * @param tokenId token de la sesión actual del usuario para cerrarla
     */
    @Override
    public void logout(String tokenId) {
        tokenRepository.deleteById(tokenId); // eliminar la sesión actual = cerrar la sesión
    }


    /**
     * @param appUser usuario a borrar o dar de baja definitivamente
     */
    @Override
    public void delete(AppUser appUser) {
        userRepository.delete(appUser); // eliminar el usuario
    }

}
