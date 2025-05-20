package com.icai.proyectofinal.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icai.proyectofinal.model.user.RegisterRequest;
import com.icai.proyectofinal.model.user.ProfileRequest;
import com.icai.proyectofinal.model.review.ReviewRegister;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EndToEndTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void flujoCompletoUsuario() throws Exception {
        // 1. Registro
        var register = new RegisterRequest("e2euser", "e2e@test.com", "Password1", "Password1", "Nombre E2E", "OWNER");
        mockMvc.perform(post("/usuario/nuevo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        // 2. Login
        var login = mockMvc.perform(post("/usuario/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "e2e@test.com")
                .param("password", "Password1"))
                .andExpect(status().isOk())
                .andReturn();
        var sessionHeader = login.getResponse().getHeader("Set-Cookie");
        String sessionId = sessionHeader.split("=")[1].split(";")[0];

        // 3. Crear restaurante
        String restJson = "{" +
                "\"name_restaurant\":\"E2E Restaurante\"," +
                "\"phone\":\"123456789\"," +
                "\"tipo\":\"FUSION\"," +
                "\"direction\":\"Calle E2E\"," +
                "\"latitude\":\"40.0\"," +
                "\"longitude\":\"-3.0\"}";
        mockMvc.perform(post("/restaurantes/nuevo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(restJson)
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isCreated());

        // 3b. Consultar todos los restaurantes
        mockMvc.perform(get("/restaurantes/mostrar/todos")
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name_restaurant == 'E2E Restaurante')]").exists());

        // 4. Filtrar restaurantes
        mockMvc.perform(get("/restaurantes/filtrar?tipo=FUSION")
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name_restaurant == 'E2E Restaurante')]").exists());

        // 5. Crear reseña
        var review = new ReviewRegister("Muy bueno", 5);
        // Obtener el id del restaurante creado (ya se obtiene más abajo)
        var restList = mockMvc.perform(get("/restaurantes/filtrar?tipo=FUSION")
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isOk())
                .andReturn();
        String restId = objectMapper.readTree(restList.getResponse().getContentAsString()).get(0).get("id").asText();
        mockMvc.perform(post("/review/nuevo")
                .param("restauranteId", restId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review))
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isCreated());

        // 5b. Consultar reseñas del restaurante
        String restJsonId = "{" +
                "\"id\":\"" + restId + "\"}";
        mockMvc.perform(get("/review/filtrar/restaurante")
                .contentType(MediaType.APPLICATION_JSON)
                .content(restJsonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Muy bueno"));

        // 6. Listar reseñas del usuario
        mockMvc.perform(get("/review/filtrar/usuario")
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Muy bueno"));

        // 7. Actualizar perfil
        var profile = new ProfileRequest("NuevoNombre", null, "Password1");
        mockMvc.perform(put("/usuario/yo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profile))
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isOk());

        // 8. Eliminar cuenta
        mockMvc.perform(delete("/usuario/yo")
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isOk());

        // 9. Logout y comprobar acceso denegado
        mockMvc.perform(post("/usuario/logout")
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/usuario/yo")
                .cookie(new Cookie("session", sessionId)))
                .andExpect(status().isUnauthorized());
    }
}

