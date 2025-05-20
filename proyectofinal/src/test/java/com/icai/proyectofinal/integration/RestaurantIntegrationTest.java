package com.icai.proyectofinal.integration;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.Type;
import com.icai.proyectofinal.repository.RestaurantRepository;
import com.icai.proyectofinal.repository.UserRepository;
import com.icai.proyectofinal.model.user.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RestaurantIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
        AppUser owner = new AppUser();
        for (String[] field : new String[][]{
                {"name_user", "owner1"},
                {"email", "owner1@test.com"},
                {"password", "pass"},
                {"role", "OWNER"},
                {"name", "Dueño Uno"}}) {
            java.lang.reflect.Field f = AppUser.class.getDeclaredField(field[0]);
            f.setAccessible(true);
            f.set(owner, field[1]);
        }
        userRepository.save(owner);

        AppRestaurant r = new AppRestaurant();
        r.setOwner(owner);
        r.setName_restaurant("Restaurante Test");
        r.setDirection("Calle 1");
        r.setPhone("123456789");
        r.setType(Type.FUSION);
        r.setLatitude("40.0");
        r.setLongitude("-3.0");
        restaurantRepository.save(r);
    }

    @Test
    void filtrarRestaurantes_devuelveLista() throws Exception {
        mockMvc.perform(get("/restaurantes/filtrar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name_restaurant").value("Restaurante Test"));
    }

    @Test
    void filtrarRestaurantes_porTipo() throws Exception {
        mockMvc.perform(get("/restaurantes/filtrar?tipo=FUSION")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("FUSION"));
    }

    @Test
    void crearRestaurante_postNuevoDevuelve201() throws Exception {
        // Registrar usuario OWNER y login
        RegisterRequest req = new RegisterRequest("owner2", "owner2@test.com", "Password1", "Password1", "Dueño Dos", "OWNER");
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/usuario/nuevo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
        MvcResult login = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/usuario/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "owner2@test.com")
                .param("password", "Password1"))
                .andExpect(status().isOk())
                .andReturn();
        String session = login.getResponse().getHeader("Set-Cookie");
        String sessionId = session.split("=")[1].split(";")[0];
        String json = "{" +
                "\"name_restaurant\":\"Nuevo Restaurante\"," +
                "\"phone\":\"987654321\"," +
                "\"tipo\":\"FUSION\"," +
                "\"direction\":\"Calle Nueva\"," +
                "\"latitude\":\"41.0\"," +
                "\"longitude\":\"-4.0\"}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/restaurantes/nuevo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isCreated());
    }

    @Test
    void filtrarRestaurantes_porMinScore() throws Exception {
        mockMvc.perform(get("/restaurantes/filtrar?minScore=0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].averageScore").exists());
    }

    @Test
    void filtrarRestaurantes_tipoInvalido_devuelve400() throws Exception {
        mockMvc.perform(get("/restaurantes/filtrar?tipo=INVALID")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
