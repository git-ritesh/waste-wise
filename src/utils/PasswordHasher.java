package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification
 */
public class PasswordHasher {
    
    /**
     * Hash a password using SHA-256 with salt
     * @param password Plain text password
     * @return Hashed password with salt
     */
   
    public static String hashPassword(String password) {
        try {
            // Generate a random 16-byte salt
            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
    
            // Hash the password with the salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
    
            // Combine salt and hashed password
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);
    
            // Encode as Base64 string
            return Base64.getEncoder().encodeToString(combined);
    
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }    
    
    /**
     * Check if a plain text password matches a hashed password
     * @param plainPassword Plain text password
     * @param hashedPassword Hashed password with salt
     * @return true if the passwords match, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            // Decode the combined salt and hashed password
            byte[] combined = Base64.getDecoder().decode(hashedPassword);
            
            // Extract the salt (first 16 bytes)
            byte[] salt = new byte[16];
            System.arraycopy(combined, 0, salt, 0, salt.length);
            
            // Hash the plain password with the extracted salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPlainPassword = md.digest(plainPassword.getBytes());
            
            // Compare the hashed plain password with the stored hashed password
            byte[] storedHashedPassword = new byte[combined.length - salt.length];
            System.arraycopy(combined, salt.length, storedHashedPassword, 0, storedHashedPassword.length);
            
            // Compare the two hashed passwords
            if (hashedPlainPassword.length != storedHashedPassword.length) {
                return false;
            }
            
            for (int i = 0; i < hashedPlainPassword.length; i++) {
                if (hashedPlainPassword[i] != storedHashedPassword[i]) {
                    return false;
                }
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error checking password: " + e.getMessage());
            return false;
        }
    }
}