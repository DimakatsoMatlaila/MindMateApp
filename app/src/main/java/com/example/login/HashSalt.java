package com.example.login;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
public class HashSalt {
    private static final int SALT_LENGTH = 2;

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        StringBuilder saltString = new StringBuilder();

        for (byte b : salt) {
            String hex = String.format("%02x", b);
            saltString.append(hex);
        }

        return saltString.toString();
    }

    public static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] saltedInput = (input).getBytes(StandardCharsets.UTF_8);
            byte[] encodedHash = digest.digest(saltedInput);
            StringBuilder hexString = new StringBuilder();

            for (byte b : encodedHash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }
}
