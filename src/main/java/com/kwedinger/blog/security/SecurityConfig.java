package com.kwedinger.blog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final CustomAuthenticationProvider customAuthenticationProvider;
    
    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(customAuthenticationProvider));
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
            )
            .authorizeHttpRequests(auth -> auth
                // Public routes
                .requestMatchers("/", "/blog", "/blog/**", "/presentations", "/about", 
                                "/session/new", "/session", "/up").permitAll()
                // Admin routes require authentication
                .requestMatchers("/admin/**").authenticated()
                // All other routes require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/session/new")
                .loginProcessingUrl("/session")
                .defaultSuccessUrl("/admin", true)
                .failureUrl("/session/new?error=true")
                .authenticationManager(authenticationManager())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/session")
                .logoutSuccessUrl("/session/new")
                .invalidateHttpSession(true)
                .deleteCookies("session_id")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/session") // Allow POST to /session for login
            );
        
        return http.build();
    }
}
