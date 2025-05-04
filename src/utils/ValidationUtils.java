package utils;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[0-9]{10,15}$"
    );
    
    /**
     * Validate an email address
     * @param email Email address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate a phone number
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validate a username
     * @param username Username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.trim().length() >= 4 && username.trim().length() <= 20;
    }
    
    /**
     * Validate a password
     * @param password Password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    /**
     * Validate a name
     * @param name Name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() <= 100;
    }
    
    /**
     * Validate a quantity
     * @param quantity Quantity to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidQuantity(String quantity) {
        try {
            double value = Double.parseDouble(quantity);
            return value > 0 && value <= 1000; // Assuming max 1000 kg
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate an address
     * @param address Address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty() && address.trim().length() <= 500;
    }
    
    /**
     * Validate a rating
     * @param rating Rating to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidRating(String rating) {
        try {
            int value = Integer.parseInt(rating);
            return value >= 1 && value <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}