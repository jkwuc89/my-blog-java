package com.kwedinger.blog.controller.admin;

import com.kwedinger.blog.model.Presentation;
import com.kwedinger.blog.repository.ConferenceRepository;
import com.kwedinger.blog.repository.PresentationRepository;
import com.kwedinger.blog.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminPresentationsController extends AdminBaseController {
    private final PresentationRepository presentationRepository;
    private final ConferenceRepository conferenceRepository;
    private final FileService fileService;
    
    public AdminPresentationsController(PresentationRepository presentationRepository,
                                       ConferenceRepository conferenceRepository,
                                       FileService fileService) {
        this.presentationRepository = presentationRepository;
        this.conferenceRepository = conferenceRepository;
        this.fileService = fileService;
    }
    
    @GetMapping("/presentations")
    public String index(Model model) {
        List<Presentation> presentations = presentationRepository.findAll().stream()
            .sorted((p1, p2) -> p1.getTitle().compareToIgnoreCase(p2.getTitle()))
            .toList();
        model.addAttribute("presentations", presentations);
        return "admin/presentations/index";
    }
    
    @GetMapping("/presentations/{id}")
    public String show(@PathVariable Long id, Model model) {
        Presentation presentation = presentationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("presentation", presentation);
        return "admin/presentations/show";
    }
    
    @GetMapping("/presentations/new")
    public String newPresentation(Model model) {
        model.addAttribute("presentation", new Presentation());
        model.addAttribute("availableFiles", fileService.getAvailablePresentationFiles());
        model.addAttribute("conferences", conferenceRepository.findAll());
        return "admin/presentations/new";
    }
    
    @PostMapping("/presentations")
    public String create(@ModelAttribute Presentation presentation,
                        @RequestParam(required = false) List<Long> conferenceIds,
                        RedirectAttributes redirectAttributes) {
        if (conferenceIds != null) {
            presentation.setConferences(conferenceIds.stream()
                .map(conferenceRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .toList());
        }
        presentationRepository.save(presentation);
        redirectAttributes.addFlashAttribute("notice", "Presentation created successfully.");
        return "redirect:/admin/presentations/" + presentation.getId();
    }
    
    @GetMapping("/presentations/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Presentation presentation = presentationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("presentation", presentation);
        model.addAttribute("availableFiles", fileService.getAvailablePresentationFiles());
        model.addAttribute("conferences", conferenceRepository.findAll());
        return "admin/presentations/edit";
    }
    
    @PostMapping("/presentations/{id}")
    public String update(@PathVariable Long id,
                        @ModelAttribute Presentation presentation,
                        @RequestParam(required = false) List<Long> conferenceIds,
                        RedirectAttributes redirectAttributes) {
        Presentation existingPresentation = presentationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existingPresentation.setTitle(presentation.getTitle());
        existingPresentation.setAbstractText(presentation.getAbstractText());
        existingPresentation.setSlidesUrl(presentation.getSlidesUrl());
        existingPresentation.setGithubUrl(presentation.getGithubUrl());
        if (conferenceIds != null) {
            existingPresentation.setConferences(conferenceIds.stream()
                .map(conferenceRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .toList());
        } else {
            existingPresentation.setConferences(List.of());
        }
        presentationRepository.save(existingPresentation);
        redirectAttributes.addFlashAttribute("notice", "Presentation updated successfully.");
        return "redirect:/admin/presentations/" + id;
    }
    
    @PostMapping("/presentations/{id}/delete")
    public String destroy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        presentationRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("notice", "Presentation deleted successfully.");
        return "redirect:/admin/presentations";
    }
}
