package com.kwedinger.blog.controller.admin;

import com.kwedinger.blog.model.Bio;
import com.kwedinger.blog.service.BioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminBioController extends AdminBaseController {
    private final BioService bioService;
    
    public AdminBioController(BioService bioService) {
        this.bioService = bioService;
    }
    
    @GetMapping("/bio")
    public String show(Model model) {
        Bio bio = bioService.getInstance();
        model.addAttribute("bio", bio);
        return "admin/bio/show";
    }
    
    @GetMapping("/bio/edit")
    public String edit(Model model) {
        Bio bio = bioService.getInstance();
        model.addAttribute("bio", bio);
        return "admin/bio/edit";
    }
    
    @PostMapping("/bio")
    public String update(@ModelAttribute Bio bio, RedirectAttributes redirectAttributes) {
        Bio existingBio = bioService.getInstance();
        existingBio.setName(bio.getName());
        existingBio.setBriefBio(bio.getBriefBio());
        existingBio.setContent(bio.getContent());
        bioService.save(existingBio);
        redirectAttributes.addFlashAttribute("notice", "Bio updated successfully.");
        return "redirect:/admin/bio";
    }
}
