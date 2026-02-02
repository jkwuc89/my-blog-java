package com.kwedinger.blog.controller;

import com.kwedinger.blog.model.Bio;
import com.kwedinger.blog.model.ContactInfo;
import com.kwedinger.blog.service.BioService;
import com.kwedinger.blog.service.ContactInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {
    private final BioService bioService;
    private final ContactInfoService contactInfoService;
    
    public PagesController(BioService bioService, ContactInfoService contactInfoService) {
        this.bioService = bioService;
        this.contactInfoService = contactInfoService;
    }
    
    @GetMapping("/about")
    public String about(Model model) {
        Bio bio = bioService.getInstance();
        ContactInfo contactInfo = contactInfoService.getInstance();
        model.addAttribute("bio", bio);
        model.addAttribute("contactInfo", contactInfo);
        return "pages/about";
    }
}
