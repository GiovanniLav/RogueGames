package com.roguegames.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
@Controller
public class StaticPageController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();
        return "Logout";  // Reindirizza alla home
    }
}
