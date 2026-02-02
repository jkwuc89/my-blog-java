package com.kwedinger.blog.controller.admin;

import com.kwedinger.blog.model.ContactInfo;
import com.kwedinger.blog.service.ContactInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminContactInfoController extends AdminBaseController {
    private final ContactInfoService contactInfoService;
    
    public AdminContactInfoController(ContactInfoService contactInfoService) {
        this.contactInfoService = contactInfoService;
    }
    
    @GetMapping("/contact_info")
    public String show(Model model) {
        ContactInfo contactInfo = contactInfoService.getInstance();
        model.addAttribute("contactInfo", contactInfo);
        return "admin/contact_info/show";
    }
    
    @GetMapping("/contact_info/edit")
    public String edit(Model model) {
        ContactInfo contactInfo = contactInfoService.getInstance();
        model.addAttribute("contactInfo", contactInfo);
        return "admin/contact_info/edit";
    }
    
    @PostMapping("/contact_info")
    public String update(@ModelAttribute ContactInfo contactInfo, RedirectAttributes redirectAttributes) {
        ContactInfo existingContactInfo = contactInfoService.getInstance();
        existingContactInfo.setEmail(contactInfo.getEmail());
        existingContactInfo.setGithubUrl(contactInfo.getGithubUrl());
        existingContactInfo.setLinkedinUrl(contactInfo.getLinkedinUrl());
        existingContactInfo.setTwitterUrl(contactInfo.getTwitterUrl());
        existingContactInfo.setUntappedUrl(contactInfo.getUntappedUrl());
        contactInfoService.save(existingContactInfo);
        redirectAttributes.addFlashAttribute("notice", "Contact info updated successfully.");
        return "redirect:/admin/contact_info";
    }
}
