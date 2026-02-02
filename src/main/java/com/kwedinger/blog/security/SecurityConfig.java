package com.kwedinger.blog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.kwedinger.blog.config.RequestLoggingFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final CustomAuthenticationProvider customAuthenticationProvider;
    
    public SecurityConfig(@Lazy CustomAuthenticationProvider customAuthenticationProvider) {
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RequestLoggingFilter requestLoggingFilter) throws Exception {
        http
            .addFilterBefore(requestLoggingFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
            )
            .authorizeHttpRequests(auth -> auth
                // Static resources (images, CSS, JS, documents, etc.) - must come before other rules
                .requestMatchers("/profile_photo.png", "/icon_180x180.png", "/**/*.png", "/**/*.jpg", 
                                "/**/*.jpeg", "/**/*.gif", "/**/*.svg", "/**/*.ico", "/**/*.css", 
                                "/**/*.js", "/**/*.pdf", "/**/*.pptx", "/**/*.md",
                                "/css/**", "/documents/**", "/blog_posts/**", "/presentations/**",
                                "/email.svg", "/github.svg", "/linkedin.svg", "/twitter.svg", "/Untappd.svg",
                                "/download.svg", "/robots.txt").permitAll()
                // Public routes
                .requestMatchers("/", "/blog", "/blog/**", "/presentations", "/about", 
                                "/session/new", "/session", "/logout", "/up").permitAll()
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
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/session/new")
                .invalidateHttpSession(true)
                .deleteCookies("session_id")
                .permitAll()
            );
            // CSRF is enabled by default for Spring Security form login
            // The CSRF token is automatically included in Thymeleaf forms via th:action
        
        return http.build();
    }
}
