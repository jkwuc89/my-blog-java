package com.kwedinger.blog.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    private static final String BLOG_POSTS_DIR = "static/blog_posts";
    private static final String PRESENTATIONS_DIR = "static/presentations";
    
    public List<String> getAvailableBlogPostFiles() {
        try {
            ClassPathResource resource = new ClassPathResource(BLOG_POSTS_DIR);
            if (!resource.exists()) {
                return List.of();
            }
            
            Path blogPostsPath = Paths.get(resource.getURI());
            return Files.list(blogPostsPath)
                .filter(path -> path.toString().endsWith(".md"))
                .map(path -> path.getFileName().toString())
                .sorted()
                .collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }
    
    public List<String> getAvailablePresentationFiles() {
        try {
            ClassPathResource resource = new ClassPathResource(PRESENTATIONS_DIR);
            if (!resource.exists()) {
                return List.of();
            }
            
            Path presentationsPath = Paths.get(resource.getURI());
            return Files.list(presentationsPath)
                .filter(path -> path.toString().endsWith(".pptx"))
                .map(path -> path.getFileName().toString())
                .sorted()
                .collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }
}
