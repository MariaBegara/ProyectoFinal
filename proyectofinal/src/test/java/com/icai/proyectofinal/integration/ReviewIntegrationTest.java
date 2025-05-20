package com.icai.proyectofinal.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.model.Type;
import com.icai.proyectofinal.model.review.ReviewRegister;
import com.icai.proyectofinal.model.user.RegisterRequest;
import com.icai.proyectofinal.repository.RestaurantRepository;
import com.icai.proyectofinal.repository.UserRepository;
import com.icai.proyectofinal.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewIntegrationTest {
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

    private String session;
    private String restauranteId;

    @BeforeEach
    void setUp() throws Exception {
        // Eliminar primero reviews, luego restaurantes, luego usuarios
        reviewRepository.deleteAll();
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        // Registrar usuario y login
        RegisterRequest req = new RegisterRequest("user1", "user1@test.com", "Password1", "Password1", "Nombre", "USER");
        mockMvc.perform(post("/usuario/nuevo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
        var login = mockMvc.perform(post("/usuario/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "user1@test.com")
                .param("password", "Password1"))
                .andExpect(status().isOk())
                .andReturn();
        session = login.getResponse().getHeader("Set-Cookie");
        String sessionId = session.split("=")[1].split(";")[0];
        // Crear restaurante
        String restJson = "{" +
                "\"name_restaurant\":\"Restaurante Review\"," +
                "\"phone\":\"123456789\"," +
                "\"tipo\":\"FUSION\"," +
                "\"direction\":\"Calle Review\"," +
                "\"latitude\":\"40.0\"," +
                "\"longitude\":\"-3.0\"}";
        mockMvc.perform(post("/restaurantes/nuevo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(restJson)
                .cookie(new jakarta.servlet.http.Cookie("session", sessionId)))
                .andExpect(status().isCreated());
        AppRestaurant rest = restaurantRepository.findAll().get(0);
        restauranteId = rest.getId();
    }

    @Test
    void crearReview_autenticado_devuelve201() throws Exception {
        ReviewRegister reg = new ReviewRegister("Muy bueno", 5);
        String json = objectMapper.writeValueAsString(reg);
        String sessionId = session.split("=")[1].split(";")[0];
        mockMvc.perform(post("/review/nuevo")
                .param("restauranteId", restauranteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new jakarta.servlet.http.Cookie("session", sessionId)))
                .andExpect(status().isCreated());
    }

    @Test
    void listarReviewsUsuario_autenticado_devuelveLista() throws Exception {
        // Crear review primero
        ReviewRegister reg = new ReviewRegister("Excelente", 5);
        String json = objectMapper.writeValueAsString(reg);
        String sessionId = session.split("=")[1].split(";")[0];
        mockMvc.perform(post("/review/nuevo")
                .param("restauranteId", restauranteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new jakarta.servlet.http.Cookie("session", sessionId)))
                .andExpect(status().isCreated());
        // Listar reviews del usuario
        mockMvc.perform(get("/review/filtrar/usuario")
                .cookie(new jakarta.servlet.http.Cookie("session", sessionId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Excelente"));
    }

    @Test
    void crearReview_noAutenticado_devuelve401() throws Exception {
        ReviewRegister reg = new ReviewRegister("Sin login", 3);
        String json = objectMapper.writeValueAsString(reg);
        mockMvc.perform(post("/review/nuevo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void actualizarReview_putDevuelve200() throws Exception {
        // Crear review
        ReviewRegister reg = new ReviewRegister("Original", 4);
        String json = objectMapper.writeValueAsString(reg);
        String sessionId = session.split("=")[1].split(";")[0];
        var result = mockMvc.perform(post("/review/nuevo")
                .param("restauranteId", restauranteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new jakarta.servlet.http.Cookie("session", sessionId)))
                .andExpect(status().isCreated())
                .andReturn();
        // Obtener el id de la review creada
        String responseBody = result.getResponse().getContentAsString();
        String reviewId = objectMapper.readTree(responseBody).get("id").asText();
        // Actualizar review
        String updateJson = "{" +
                "\"reviewId\":\"" + reviewId + "\"," +
                "\"content\":\"Actualizado\"," +
                "\"score\":5}";
        mockMvc.perform(put("/review/filtrar/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Actualizado"))
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    void listarReviewsRestaurante_getDevuelveLista() throws Exception {
        // Crear review
        ReviewRegister reg = new ReviewRegister("Para restaurante", 4);
        String json = objectMapper.writeValueAsString(reg);
        String sessionId = session.split("=")[1].split(";")[0];
        mockMvc.perform(post("/review/nuevo")
                .param("restauranteId", restauranteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new jakarta.servlet.http.Cookie("session", sessionId)))
                .andExpect(status().isCreated());
        // Consultar reviews del restaurante
        String restJson = "{" +
                "\"id\":\"" + restauranteId + "\"}";
        mockMvc.perform(get("/review/filtrar/restaurante")
                .contentType(MediaType.APPLICATION_JSON)
                .content(restJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Para restaurante"));
    }
}
