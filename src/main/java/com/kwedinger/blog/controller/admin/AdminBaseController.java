package com.kwedinger.blog.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
public abstract class AdminBaseController {
    // Base class for all admin controllers
    // All admin routes require authentication (enforced by Spring Security)
}
