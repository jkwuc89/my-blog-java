package com.kwedinger.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafDialectConfig {
    
    // ViewHelper will be available in templates via @{viewHelper.method()}
    // We'll add it to the model in controllers or use it via @Component
}
