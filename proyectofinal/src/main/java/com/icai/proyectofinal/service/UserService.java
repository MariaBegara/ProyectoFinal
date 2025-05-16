package com.icai.proyectofinal.service;

import com.icai.proyectofinal.entity.Token;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.ProfileRequest;
import com.icai.proyectofinal.model.ProfileResponse;
import com.icai.proyectofinal.model.RegisterRequest;
import com.icai.proyectofinal.repository.TokenRepository;
import com.icai.proyectofinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface{

    @Autowired
    private UserRepository userRepository; // es necesario para poder utilizar los métodos de AppUserRepository
    @Autowired
    private TokenRepository tokenRepository; // es necesario para poder utilizar los métodos de TokenRepository
    //@Autowired
    //private final Hashing hashing = new Hashing();
    /**
     * @param email email proporcionado para el login
     * @param password password proporcionado para el login
     * @return si las credenciales del usuario son correctas, retorna un token de sesión asociado a dicho usuario;
     * si las credenciales son incorrectas, retorna null
     */
    @Override
    public Token login(String email, String password) {

        //System.out.println("Login con email: '" + email + "' y contraseña: '" + password + "'");

        Optional<AppUser> appUser = userRepository.findByEmail(email);

        //if ((appUser.isEmpty()) || (!hashing.compare(appUser.get().password, password))) return null; // si las credenciales son incorrectas, retorna null
        if ((appUser.isEmpty()) || (!Objects.equals(appUser.get().password, password))) return null;
        //System.out.println("usuario encontrado: '" + appUser.email + "' y contraseña en BD: " + appUser.password);

        Token token = tokenRepository.findByAppUser(appUser); //ES <OPTIONAL>!! fix
        if (token != null) return token; // @return si las credenciales del usuario son correctas, retorna un token de sesión asociado a dicho usuario;

        // Crear nuevo token si no existe uno previo
        Token token_nuevo = new Token();
        tokenRepository.save(token_nuevo); // guardarlo
        return token_nuevo;
    }


    /**
     * @param tokenId token de la sesión actual del usuario
     * @return si la sesión está creada (el token existe en BD), retorna el usuario asociado a dicha sesión;
     * si la sesión no existe, retorna null
     */
    @Override
    public AppUser authenticate(String tokenId) {
        Optional<Token> tokenOpt = tokenRepository.findById(tokenId);

        if (tokenOpt.isPresent()) {
            Token token = tokenOpt.get();
            return token.appUser;
        } else {
            return null;
        }
    }


    /**
     * @param appUser usuario
     * @return respuesta con el perfil de dicho usuario
     */
    @Override
    public ProfileResponse profile(AppUser appUser) {
        return new ProfileResponse(appUser.name, appUser.email, appUser.role); // respuesta con el perfil de dicho usuario
    }


    /**
     * @param appUser usuario
     * @param profile nuevos datos para el perfil del usuario
     * @return respuesta con el perfil de dicho usuario actualizado
     */
    @Override
    public ProfileResponse updateProfile(AppUser appUser, ProfileRequest profile) {

        if ((profile.name() != null) && (!profile.name().isEmpty())) {
            appUser.name = profile.name();
        }

        /*if ((Objects.equals(appUser.name, profile.name())) && (!hashing.compare(appUser.password, profile.password()))) {
            appUser.password = hashing.hash(profile.password()); // Si el nombre coincide y se cambia la contraseña, esta se debe actualizar -> hasheada
        }*/
        if ((Objects.equals(appUser.name, profile.name())) && (Objects.equals(appUser.password, profile.password()))) {
            appUser.password = profile.password(); // Si el nombre coincide y se cambia la contraseña, esta se debe actualizar -> hasheada
        }

        userRepository.save(appUser); // Se actualiza el usuario con el cambio realizado
        return profile(appUser);
    }


    /**
     *
     * @param register datos de registro del usuario
     * @return respuesta con el perfil del nuevo usuario:
     */
    @Override
    public ProfileResponse register(RegisterRequest register) {
        // register tiene todos los datos del usuario
        AppUser usuario = new AppUser();
        //String password_hashed = hashing.hash(register.password());
        usuario.name = register.name();
        usuario.email = register.email();
        //usuario.password = password_hashed; // Se debe guardar hasheada
        usuario.password = register.password1();
        usuario.role = register.role();
        userRepository.save(usuario);

        return profile(usuario); // respuesta con el perfil del nuevo usuario
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
