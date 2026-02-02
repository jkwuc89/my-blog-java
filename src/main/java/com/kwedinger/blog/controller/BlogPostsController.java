package com.kwedinger.blog.controller;

import com.kwedinger.blog.model.BlogPost;
import com.kwedinger.blog.repository.BlogPostRepository;
import com.kwedinger.blog.service.BlogPostFileReader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Controller
public class BlogPostsController {
    private final BlogPostRepository blogPostRepository;
    private final BlogPostFileReader blogPostFileReader;
    
    public BlogPostsController(BlogPostRepository blogPostRepository, BlogPostFileReader blogPostFileReader) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostFileReader = blogPostFileReader;
    }
    
    @GetMapping({"/", "/blog"})
    public String index(Model model) {
        List<BlogPost> blogPosts = blogPostRepository.findPublishedRecent(LocalDate.now());
        model.addAttribute("blogPosts", blogPosts);
        return "blog_posts/index";
    }
    
    @GetMapping("/blog/{filename}")
    public String show(@PathVariable String filename, Model model) {
        // If filename doesn't have .md extension, add it
        if (!filename.endsWith(".md")) {
            filename = filename + ".md";
        }
        
        BlogPost blogPost = blogPostRepository.findByFilename(filename)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog post not found"));
        
        String blogContent = blogPostFileReader.readContent(blogPost.getFilename());
        if (blogContent == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog post not found");
        }
        
        model.addAttribute("blogPost", blogPost);
        model.addAttribute("blogContent", blogContent);
        return "blog_posts/show";
    }
}
