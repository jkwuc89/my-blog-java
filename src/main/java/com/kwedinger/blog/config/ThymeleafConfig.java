package com.kwedinger.blog.config;

import com.kwedinger.blog.service.ContactInfoService;
import com.kwedinger.blog.service.FileService;
import com.kwedinger.blog.service.MarkdownService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {
    
    @Bean
    public ViewHelper viewHelper(MarkdownService markdownService, FileService fileService, ContactInfoService contactInfoService) {
        return new ViewHelper(markdownService, fileService, contactInfoService);
    }
}
