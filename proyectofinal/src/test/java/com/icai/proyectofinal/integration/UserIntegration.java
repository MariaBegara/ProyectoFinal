package com.icai.proyectofinal.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icai.proyectofinal.model.user.RegisterRequest;
import com.icai.proyectofinal.repository.UserRepository;
import com.icai.proyectofinal.repository.RestaurantRepository;
import com.icai.proyectofinal.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registroYLoginUsuario() throws Exception {
        RegisterRequest req = new RegisterRequest(
                "usuario1",
                "usuario1@test.com",
                "Password1",
                "Password1",
                "Nombre Real",
                "USER"
        );
        // Registro
        mockMvc.perform(post("/usuario/nuevo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("usuario1@test.com"));

        // Login
        mockMvc.perform(post("/usuario/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "usuario1@test.com")
                        .param("password", "Password1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void perfilUsuario_autenticado_devuelvePerfil() throws Exception {
        RegisterRequest req = new RegisterRequest(
                "usuario2",
                "usuario2@test.com",
                "Password1",
                "Password1",
                "Nombre Real",
                "USER"
        );
        // Registro
        mockMvc.perform(post("/usuario/nuevo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
        // Login y obtener sesión
        var login = mockMvc.perform(post("/usuario/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "usuario2@test.com")
                        .param("password", "Password1"))
                .andExpect(status().isOk())
                .andReturn();
        var session = login.getResponse().getHeader("Set-Cookie");
        // Extraer solo el valor del sessionId de la cookie
        String sessionId = session.split("=")[1].split(";")[0];
        // Consultar perfil autenticado
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/usuario/yo")
                        .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("usuario2@test.com"));
    }

    @Test
    void eliminarUsuario_autenticado_devuelve200() throws Exception {
        RegisterRequest req = new RegisterRequest(
                "usuario3",
                "usuario3@test.com",
                "Password1",
                "Password1",
                "Nombre Real",
                "USER"
        );
        mockMvc.perform(post("/usuario/nuevo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
        var login = mockMvc.perform(post("/usuario/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "usuario3@test.com")
                        .param("password", "Password1"))
                .andExpect(status().isOk())
                .andReturn();
        var session = login.getResponse().getHeader("Set-Cookie");
        String sessionId = session.split("=")[1].split(";")[0];
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/usuario/yo")
                        .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isOk());
    }

    @Test
    void perfilUsuario_noAutenticado_devuelve401() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/usuario/yo"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutUsuario_invalidaSesion() throws Exception {
        RegisterRequest req = new RegisterRequest(
                "usuario4",
                "usuario4@test.com",
                "Password1",
                "Password1",
                "Nombre Real",
                "USER"
        );
        // Registro
        mockMvc.perform(post("/usuario/nuevo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
        // Login y obtener sesión
        var login = mockMvc.perform(post("/usuario/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "usuario4@test.com")
                        .param("password", "Password1"))
                .andExpect(status().isOk())
                .andReturn();
        var session = login.getResponse().getHeader("Set-Cookie");
        String sessionId = session.split("=")[1].split(";")[0];
        // Logout
        mockMvc.perform(post("/usuario/logout")
                        .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isNoContent());
        // Intentar acceder al perfil después del logout debe devolver 401
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/usuario/yo")
                        .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isUnauthorized());
    }
}