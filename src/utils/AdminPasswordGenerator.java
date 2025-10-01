package utils;

public class AdminPasswordGenerator {
    public static void main(String[] args) {
        String plainPassword = "admin123";
        String hashedPassword = PasswordHasher.hashPassword(plainPassword);
        System.out.println("Hashed Password for 'admin123':");
        System.out.println(hashedPassword);
    }
}