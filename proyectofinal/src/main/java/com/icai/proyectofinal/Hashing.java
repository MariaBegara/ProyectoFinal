package com.icai.proyectofinal;


import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class Hashing {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128; // bits
    private static final int SALT_LENGTH = 16; // bytes

    /**
     * Hashea una cadena generando automáticamente una nueva salt.
     */
    public String hash(String plainText) {
        byte[] salt = generateSalt();
        return hash(plainText, salt);
    }

    /**
     * Compara si una contraseña dada coincide con la almacenada (hasheada).
     */
    public boolean compare(String hashedString, String plainText) {
        try {
            String[] parts = hashedString.split(":");
            if (parts.length != 2) return false;

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String expectedHash = hash(plainText, salt);
            return hashedString.equals(expectedHash);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Hashea una cadena con una salt específica (en bytes).
     */
    private String hash(String plainText, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el hash", e);
        }
    }

    /**
     * Genera una salt aleatoria.
     */
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
}
