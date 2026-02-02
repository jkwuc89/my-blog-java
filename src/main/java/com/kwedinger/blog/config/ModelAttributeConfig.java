package com.kwedinger.blog.config;

import com.kwedinger.blog.model.Bio;
import com.kwedinger.blog.model.ContactInfo;
import com.kwedinger.blog.service.BioService;
import com.kwedinger.blog.service.ContactInfoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ModelAttributeConfig {
    private final BioService bioService;
    private final ContactInfoService contactInfoService;
    
    public ModelAttributeConfig(BioService bioService, ContactInfoService contactInfoService) {
        this.bioService = bioService;
        this.contactInfoService = contactInfoService;
    }
    
    @ModelAttribute("bio")
    public Bio bio() {
        return bioService.getInstance();
    }
    
    @ModelAttribute("contactInfo")
    public ContactInfo contactInfo() {
        return contactInfoService.getInstance();
    }
}
