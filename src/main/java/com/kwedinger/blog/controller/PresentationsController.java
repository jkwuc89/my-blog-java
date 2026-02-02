package com.kwedinger.blog.controller;

import com.kwedinger.blog.model.Presentation;
import com.kwedinger.blog.repository.PresentationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PresentationsController {
    private final PresentationRepository presentationRepository;
    
    public PresentationsController(PresentationRepository presentationRepository) {
        this.presentationRepository = presentationRepository;
    }
    
    @GetMapping("/presentations")
    public String index(Model model) {
        List<Presentation> presentations = presentationRepository.findAll().stream()
            .sorted((p1, p2) -> p1.getTitle().compareToIgnoreCase(p2.getTitle()))
            .collect(Collectors.toList());
        model.addAttribute("presentations", presentations);
        return "presentations/index";
    }
}
