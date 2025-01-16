package com.roguegames.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        return "Home"; // Restituisce la vista "index.html"
    }

    @GetMapping("/login")
    public String login() {
        return "Login"; // Restituisce la vista "login.html"
    }
}
