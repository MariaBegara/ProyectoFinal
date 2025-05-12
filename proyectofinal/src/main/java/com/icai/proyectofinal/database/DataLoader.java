package com.icai.proyectofinal.database;
import com.icai.proyectofinal.entity.AppRestaurant;
import com.icai.proyectofinal.entity.AppUser;
import com.icai.proyectofinal.model.Type;
import com.icai.proyectofinal.repository.RestaurantRepository;
import com.icai.proyectofinal.repository.UserRepository;
import com.icai.proyectofinal.service.RestaurantService;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        // Crear o recuperar el usuario "unknown"
        AppUser unknownUser = userRepository.findByEmail("unknown@system.local")
                .orElseGet(() -> {
                    AppUser user = new AppUser();
                    user.name_user = "unknown";
                    user.email = "unknown@system.local";
                    user.password = "1234";
                    user.role = "NONE";
                    user.name = "Desconocido";
                    return userRepository.save(user);
                });

        // Leer CSV con OpenCSV
        CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource("restaurantes.csv").getInputStream(), StandardCharsets.UTF_8));

        String[] campos;
        reader.readNext(); // saltar cabecera

        while ((campos = reader.readNext()) != null) {
            if (campos.length < 8) continue;

            String idOwner = campos[1];
            AppUser owner = userRepository.findById(idOwner).orElse(unknownUser);

            try {
                AppRestaurant restaurant = new AppRestaurant();
                restaurant.setOwner(owner);
                restaurant.setName_restaurant(campos[2]);
                restaurant.setDirection(campos[3]);
                restaurant.setPhone(campos[4]);

                try {
                    restaurant.setType(Type.valueOf(campos[5].trim().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Tipo inválido: " + campos[5] + " → se usará OTHER");
                    restaurant.setType(Type.OTHER);
                }

                restaurant.setLatitude(campos[6]);
                restaurant.setLongitude(campos[7]);

                restaurantService.saveRestaurant(restaurant);

            } catch (Exception e) {
                System.err.println("Error al procesar línea: " + Arrays.toString(campos));
                e.printStackTrace();
            }
        }
    }

}





