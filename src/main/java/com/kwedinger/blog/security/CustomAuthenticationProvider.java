package com.kwedinger.blog.security;

import com.kwedinger.blog.model.User;
import com.kwedinger.blog.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Lazy
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public CustomAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String emailAddress = authentication.getName().toLowerCase().strip();
        String password = authentication.getCredentials().toString();
        
        System.out.println("Attempting authentication for: " + emailAddress);
        
        User user = userRepository.findByEmailAddress(emailAddress)
            .orElseThrow(() -> {
                System.out.println("User not found: " + emailAddress);
                return new BadCredentialsException("Invalid email address or password");
            });
        
        System.out.println("User found: " + user.getEmailAddress());
        System.out.println("Stored hash: " + user.getPasswordDigest());
        
        // Verify password against stored password_digest (BCrypt hash)
        boolean matches = passwordEncoder.matches(password, user.getPasswordDigest());
        System.out.println("Password matches: " + matches);
        
        if (!matches) {
            System.out.println("Password verification failed for: " + emailAddress);
            throw new BadCredentialsException("Invalid email address or password");
        }
        
        System.out.println("Authentication successful for: " + emailAddress);
        return new UsernamePasswordAuthenticationToken(
            user.getEmailAddress(),
            null,
            Collections.emptyList()
        );
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
