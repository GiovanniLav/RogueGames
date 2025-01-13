package com.roguegames.web.controller;

import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/utenti")
public class LoginController {

    @Autowired
    private UtenteService utenteService;

    // Mostra la pagina di login
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("error", false);
        return "login";  // Restituisce la pagina login.html
    }

    // Gestisce il login
    @PostMapping("/login")
    public String loginUtente(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session,
                              Model model) {
        Utente utente = utenteService.verificaCredenziali(email, password);
        if (utente == null) {
            model.addAttribute("error", true);
            return "login";  // Mostra la pagina di login con un messaggio di errore
        }
        session.setAttribute("utente", utente);
        return "redirect:/utenti/dashboard";  // Reindirizza alla dashboard
    }

    // Mostra la dashboard (protetta)
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login";  // Reindirizza al login se l'utente non è loggato
        }
        model.addAttribute("utente", utente);
        return "dashboard";  // Mostra la pagina di dashboard
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Invalida la sessione
        return "redirect:/utenti/login";  // Reindirizza al login
    }
}
