package com.kwedinger.blog.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {
    // ViewHelper is already a @Component, so Spring will create it automatically
    // No need to define it as a @Bean here
}
