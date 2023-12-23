package com.lab.model.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout() {
        /* Make your own custom logout page if you want */
        return "redirect:/login?logout";
    }
}