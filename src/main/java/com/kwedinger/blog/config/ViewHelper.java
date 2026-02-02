package com.kwedinger.blog.config;

import com.kwedinger.blog.model.ContactInfo;
import com.kwedinger.blog.service.BlogPostFileReader;
import com.kwedinger.blog.service.ContactInfoService;
import com.kwedinger.blog.service.FileService;
import com.kwedinger.blog.service.MarkdownService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ViewHelper {
    private final MarkdownService markdownService;
    private final FileService fileService;
    private final ContactInfoService contactInfoService;
    private final BlogPostFileReader blogPostFileReader;
    
    public ViewHelper(MarkdownService markdownService, FileService fileService, 
                     ContactInfoService contactInfoService, BlogPostFileReader blogPostFileReader) {
        this.markdownService = markdownService;
        this.fileService = fileService;
        this.contactInfoService = contactInfoService;
        this.blogPostFileReader = blogPostFileReader;
    }
    
    public String adminInputClasses() {
        return "block w-full bg-white border border-gray-200 rounded-md px-4 py-3 focus:border-blue-500 transition-all";
    }
    
    public List<String> availableBlogPostFiles() {
        return fileService.getAvailableBlogPostFiles();
    }
    
    public List<String> availablePresentationFiles() {
        return fileService.getAvailablePresentationFiles();
    }
    
    public ContactInfo contactInfo() {
        return contactInfoService.getInstance();
    }
    
    public String footerIconHoverClasses() {
        return "hover:ring-2 hover:ring-blue-500 hover:ring-offset-2 rounded transition-all duration-200";
    }
    
    public String h1Classes() {
        return "text-2xl font-bold mb-3";
    }
    
    public String linkClasses() {
        return "text-blue-500 hover:underline";
    }
    
    public String markdownClasses() {
        return "max-w-none [&>p+p]:mt-6 [&_a]:text-blue-500 [&_a]:hover:underline [&_h1]:text-3xl [&_h1]:font-bold [&_h1]:mt-8 [&_h1]:mb-4 [&_h2]:text-2xl [&_h2]:font-bold [&_h2]:mt-6 [&_h2]:mb-3 [&_h3]:text-xl [&_h3]:font-semibold [&_h3]:mt-5 [&_h3]:mb-2 [&_h4]:text-lg [&_h4]:font-semibold [&_h4]:mt-4 [&_h4]:mb-2 [&_h5]:text-base [&_h5]:font-semibold [&_h5]:mt-3 [&_h5]:mb-2 [&_h6]:text-sm [&_h6]:font-semibold [&_h6]:mt-2 [&_h6]:mb-2 [&_ul]:list-disc [&_ul]:list-inside [&_ul]:my-4 [&_ul]:pl-4 [&_ol]:list-decimal [&_ol]:list-inside [&_ol]:my-4 [&_ol]:pl-4 [&_li]:mb-2 [&_li]:pl-2 [&_code]:bg-gray-100 [&_code]:px-1 [&_code]:py-0.5 [&_code]:rounded [&_code]:text-sm [&_code]:font-mono [&_pre]:bg-gray-100 [&_pre]:p-4 [&_pre]:rounded [&_pre]:overflow-x-auto [&_pre]:my-4 [&_pre_code]:bg-transparent [&_pre_code]:p-0 [&_blockquote]:border-l-4 [&_blockquote]:border-gray-300 [&_blockquote]:pl-4 [&_blockquote]:italic [&_blockquote]:my-4 [&_strong]:font-bold [&_em]:italic";
    }
    
    public String renderMarkdown(String content) {
        return markdownService.renderMarkdown(content);
    }
    
    public String safeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        return null;
    }
    
    public String blogPostExcerpt(String filename, int words) {
        return blogPostFileReader.excerpt(filename, words);
    }
}
