package com.kwedinger.blog.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java PasswordHashGenerator <password>");
            System.out.println("Example: java PasswordHashGenerator mypassword");
            System.exit(1);
        }
        
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(args[0]);
        System.out.println("Password hash: " + hash);
        System.out.println("\nSQL INSERT statement:");
        System.out.println("INSERT INTO users (email_address, password_digest, created_at, updated_at)");
        System.out.println("VALUES ('your-email@example.com', '" + hash + "', datetime('now'), datetime('now'));");
    }
}
