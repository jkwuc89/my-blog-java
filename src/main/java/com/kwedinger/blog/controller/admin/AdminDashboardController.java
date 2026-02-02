package com.kwedinger.blog.controller.admin;

import com.kwedinger.blog.repository.BlogPostRepository;
import com.kwedinger.blog.repository.ConferenceRepository;
import com.kwedinger.blog.repository.PresentationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController extends AdminBaseController {
    private final BlogPostRepository blogPostRepository;
    private final PresentationRepository presentationRepository;
    private final ConferenceRepository conferenceRepository;
    
    public AdminDashboardController(BlogPostRepository blogPostRepository,
                                   PresentationRepository presentationRepository,
                                   ConferenceRepository conferenceRepository) {
        this.blogPostRepository = blogPostRepository;
        this.presentationRepository = presentationRepository;
        this.conferenceRepository = conferenceRepository;
    }
    
    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("blogPostsCount", blogPostRepository.count());
        model.addAttribute("presentationsCount", presentationRepository.count());
        model.addAttribute("conferencesCount", conferenceRepository.count());
        return "admin/dashboard/index";
    }
}
