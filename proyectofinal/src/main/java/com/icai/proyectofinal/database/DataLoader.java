package com.icai.proyectofinal.database;

import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.Type;
import com.icai.proyectofinal.repository.UserRepository;
import com.icai.proyectofinal.service.restaurant.RestaurantServiceInterface;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RestaurantServiceInterface restaurantInterface;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear o recuperar el usuario "unknown"
        AppUser unknownUser = userRepository.findByEmail("unknown@system.local")
                .orElseGet(() -> {
                    AppUser user = new AppUser();
                    user.setName_user("unknown");
                    user.setEmail("unknown@system.local");
                    user.setPassword("1234");
                    user.setRole("NONE");
                    user.setName("Desconocido");
                    return userRepository.save(user);
                });

        // Cache temporal para evitar duplicación de dueños
        Map<String, AppUser> userCache = new HashMap<>();

        CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource("restaurantes.csv").getInputStream(), StandardCharsets.UTF_8));

        String[] campos;
        reader.readNext(); // saltar cabecera

        while ((campos = reader.readNext()) != null) {
            if (campos.length < 8) continue;

            String idOwner = campos[1].trim();
            AppUser owner = userCache.get(idOwner);

            if (owner == null) {
                // No está en cache: crear usuario nuevo y guardarlo en BD
                owner = new AppUser();
                owner.setName_user(idOwner); // usa idOwner como name_user
                owner.setEmail("autogen_" + idOwner + "@local.test");
                owner.setPassword("default");
                owner.setRole("OWNER");
                owner.setName("Generado");
                owner = userRepository.save(owner);

                userCache.put(idOwner, owner); // guardar en cache
            }

            try {
                AppRestaurant restaurant = new AppRestaurant();
                restaurant.setOwner(owner);
                restaurant.setName_restaurant(campos[2].trim());
                restaurant.setDirection(campos[3].trim());
                restaurant.setPhone(campos[4].trim());

                try {
                    restaurant.setType(Type.valueOf(campos[5].trim().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Tipo inválido: " + campos[5] + " → se usará OTHER");
                    restaurant.setType(Type.OTHER);
                }

                restaurant.setLatitude(campos[6].trim());
                restaurant.setLongitude(campos[7].trim());
                try {
                    float score = Float.parseFloat(campos[8].trim());
                    restaurant.setScore(score);
                } catch (NumberFormatException e) {
                    System.out.println("Puntuación inválida: " + campos[8] + " → se usará 0.0");
                    restaurant.setScore(0.0f);
                }

                restaurantInterface.saveRestaurant(restaurant);

            } catch (Exception e) {
                System.err.println("Error al procesar línea: " + Arrays.toString(campos));
                e.printStackTrace();
            }
        }
    }
}
