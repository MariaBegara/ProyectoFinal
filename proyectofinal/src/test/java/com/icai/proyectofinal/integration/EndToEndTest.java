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
        var session = login.getResponse().getHeader("Set-Cookie");

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
                .header("Cookie", session))
                .andExpect(status().isCreated());

        // 4. Filtrar restaurantes
        mockMvc.perform(get("/restaurantes/filtrar?tipo=FUSION")
                .header("Cookie", session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name_restaurant").value("E2E Restaurante"));

        // 5. Crear reseña
        var review = new ReviewRegister("Muy bueno", 5);
        mockMvc.perform(post("/review/nuevo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review))
                .header("Cookie", session))
                .andExpect(status().isCreated());

        // 6. Listar reseñas del usuario
        mockMvc.perform(get("/review/filtrar/usuario")
                .header("Cookie", session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Muy bueno"));

        // 7. Actualizar perfil
        var profile = new ProfileRequest("NuevoNombre", null, "Password1");
        mockMvc.perform(put("/usuario/yo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profile))
                .header("Cookie", session))
                .andExpect(status().isOk());

        // 8. Eliminar cuenta
        mockMvc.perform(delete("/usuario/yo")
                .header("Cookie", session))
                .andExpect(status().isOk());
    }
}

