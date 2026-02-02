package com.kwedinger.blog.controller.admin;

import com.kwedinger.blog.model.Conference;
import com.kwedinger.blog.repository.ConferenceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminConferencesController extends AdminBaseController {
    private final ConferenceRepository conferenceRepository;
    
    public AdminConferencesController(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }
    
    @GetMapping("/conferences")
    public String index(Model model) {
        List<Conference> conferences = conferenceRepository.findAll().stream()
            .sorted((c1, c2) -> {
                int titleCompare = c1.getTitle().compareToIgnoreCase(c2.getTitle());
                if (titleCompare != 0) return titleCompare;
                return c1.getYear().compareTo(c2.getYear());
            })
            .toList();
        model.addAttribute("conferences", conferences);
        return "admin/conferences/index";
    }
    
    @GetMapping("/conferences/{id}")
    public String show(@PathVariable Long id, Model model) {
        Conference conference = conferenceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("conference", conference);
        return "admin/conferences/show";
    }
    
    @GetMapping("/conferences/new")
    public String newConference(Model model) {
        model.addAttribute("conference", new Conference());
        return "admin/conferences/new";
    }
    
    @PostMapping("/conferences")
    public String create(@ModelAttribute Conference conference, RedirectAttributes redirectAttributes) {
        conferenceRepository.save(conference);
        redirectAttributes.addFlashAttribute("notice", "Conference created successfully.");
        return "redirect:/admin/conferences/" + conference.getId();
    }
    
    @GetMapping("/conferences/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Conference conference = conferenceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("conference", conference);
        return "admin/conferences/edit";
    }
    
    @PostMapping("/conferences/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Conference conference, RedirectAttributes redirectAttributes) {
        Conference existingConference = conferenceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existingConference.setTitle(conference.getTitle());
        existingConference.setYear(conference.getYear());
        existingConference.setLink(conference.getLink());
        conferenceRepository.save(existingConference);
        redirectAttributes.addFlashAttribute("notice", "Conference updated successfully.");
        return "redirect:/admin/conferences/" + id;
    }
    
    @PostMapping("/conferences/{id}/delete")
    public String destroy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        conferenceRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("notice", "Conference deleted successfully.");
        return "redirect:/admin/conferences";
    }
}
