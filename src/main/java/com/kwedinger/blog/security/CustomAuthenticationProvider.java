package com.kwedinger.blog.security;

import com.kwedinger.blog.model.User;
import com.kwedinger.blog.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
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
        
        User user = userRepository.findByEmailAddress(emailAddress)
            .orElseThrow(() -> new BadCredentialsException("Invalid email address or password"));
        
        // Verify password against stored password_digest (BCrypt hash)
        if (!passwordEncoder.matches(password, user.getPasswordDigest())) {
            throw new BadCredentialsException("Invalid email address or password");
        }
        
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
