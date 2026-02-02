package com.kwedinger.blog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class BlogPostFileReader {
    private static final Logger logger = LoggerFactory.getLogger(BlogPostFileReader.class);
    private static final String BLOG_POSTS_DIR = "static/blog_posts";
    
    public String readContent(String filename) {
        try {
            ClassPathResource resource = new ClassPathResource(BLOG_POSTS_DIR + "/" + filename);
            if (!resource.exists()) {
                logger.warn("Blog post file not found: {}", filename);
                return null;
            }
            return Files.readString(Paths.get(resource.getURI()));
        } catch (IOException e) {
            logger.error("Error reading blog post file {}: {}", filename, e.getMessage());
            return null;
        }
    }
    
    public String excerpt(String filename, int words) {
        String content = readContent(filename);
        if (content == null || content.isBlank()) {
            return "";
        }
        
        // Remove markdown headers and code blocks for excerpt
        String text = content
            .replaceAll("^#+\\s+", "") // Remove headers
            .replaceAll("```[\\s\\S]*?```", "") // Remove code blocks
            .replaceAll("`[^`]+`", "") // Remove inline code
            .replaceAll("\\*\\*([^*]+)\\*\\*", "$1") // Remove bold (**text**)
            .replaceAll("\\*([^*]+)\\*", "$1") // Remove italic (*text*)
            .replaceAll("\\[([^\\]]+)\\]\\([^\\)]+\\)", "$1") // Convert links to text
            .replaceAll("!\\[[^\\]]*\\]\\([^\\)]+\\)", "") // Remove images
            .strip();
        
        // Split into words and take first N words
        String[] wordArray = text.split("\\s+");
        String[] excerptWords = Arrays.stream(wordArray)
            .limit(words)
            .toArray(String[]::new);
        
        if (wordArray.length > words) {
            return String.join(" ", excerptWords) + "...";
        } else {
            return String.join(" ", excerptWords);
        }
    }
}
