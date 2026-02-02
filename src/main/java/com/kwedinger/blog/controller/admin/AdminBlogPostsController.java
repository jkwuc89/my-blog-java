package com.kwedinger.blog.controller.admin;

import com.kwedinger.blog.model.BlogPost;
import com.kwedinger.blog.repository.BlogPostRepository;
import com.kwedinger.blog.service.BlogPostFileReader;
import com.kwedinger.blog.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminBlogPostsController extends AdminBaseController {
    private final BlogPostRepository blogPostRepository;
    private final FileService fileService;
    private final BlogPostFileReader blogPostFileReader;
    
    public AdminBlogPostsController(BlogPostRepository blogPostRepository, FileService fileService, BlogPostFileReader blogPostFileReader) {
        this.blogPostRepository = blogPostRepository;
        this.fileService = fileService;
        this.blogPostFileReader = blogPostFileReader;
    }
    
    @GetMapping("/blog_posts")
    public String index(Model model) {
        List<BlogPost> blogPosts = blogPostRepository.findAll().stream()
            .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
            .toList();
        model.addAttribute("blogPosts", blogPosts);
        return "admin/blog_posts/index";
    }
    
    @GetMapping("/blog_posts/{id}")
    public String show(@PathVariable Long id, Model model) {
        BlogPost blogPost = blogPostRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String blogContent = fileService.getAvailableBlogPostFiles().contains(blogPost.getFilename()) 
            ? blogPostFileReader.readContent(blogPost.getFilename()) 
            : null;
        model.addAttribute("blogPost", blogPost);
        model.addAttribute("blogContent", blogContent);
        return "admin/blog_posts/show";
    }
    
    @GetMapping("/blog_posts/new")
    public String newBlogPost(Model model) {
        model.addAttribute("blogPost", new BlogPost());
        model.addAttribute("availableFiles", fileService.getAvailableBlogPostFiles());
        return "admin/blog_posts/new";
    }
    
    @PostMapping("/blog_posts")
    public String create(@ModelAttribute BlogPost blogPost, RedirectAttributes redirectAttributes) {
        blogPostRepository.save(blogPost);
        redirectAttributes.addFlashAttribute("notice", "Blog post created successfully.");
        return "redirect:/admin/blog_posts/" + blogPost.getId();
    }
    
    @GetMapping("/blog_posts/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        BlogPost blogPost = blogPostRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("blogPost", blogPost);
        model.addAttribute("availableFiles", fileService.getAvailableBlogPostFiles());
        return "admin/blog_posts/edit";
    }
    
    @PostMapping("/blog_posts/{id}")
    public String update(@PathVariable Long id, @ModelAttribute BlogPost blogPost, RedirectAttributes redirectAttributes) {
        BlogPost existingBlogPost = blogPostRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existingBlogPost.setTitle(blogPost.getTitle());
        existingBlogPost.setFilename(blogPost.getFilename());
        existingBlogPost.setPublishedAt(blogPost.getPublishedAt());
        blogPostRepository.save(existingBlogPost);
        redirectAttributes.addFlashAttribute("notice", "Blog post updated successfully.");
        return "redirect:/admin/blog_posts/" + id;
    }
    
    @PostMapping("/blog_posts/{id}/delete")
    public String destroy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        blogPostRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("notice", "Blog post deleted successfully.");
        return "redirect:/admin/blog_posts";
    }
}
