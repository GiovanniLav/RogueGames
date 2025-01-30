package com.roguegames.web.controller;

import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/utenti")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    // Metodo per visualizzare il form di registrazione
    @GetMapping("/registrati")
    public String showRegistrationForm(Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");
        if(utente != null){
            return "redirect:/utenti/home";
        }

        model.addAttribute("utente", new Utente());
        return "Register"; // Restituisce la pagina Register.html
    }

    // Metodo per gestire la registrazione dell'utente
    @PostMapping("/registrati")
    public String registerUtente(@Valid @ModelAttribute("utente") Utente utente, BindingResult result, Model model, HttpSession session) {
        Utente u = (Utente) session.getAttribute("utente");
        if(u != null){
            return "redirect:/utenti/home";
        }

        if (result.hasErrors()) {
            return "Register"; // Ritorna alla pagina di registrazione se ci sono errori
        }

        // Controlla se la password in chiaro soddisfa i requisiti
        if (!utente.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-])[A-Za-z\\d!@#$%^&*()_+=-]{8,}$")) {
            model.addAttribute("error", "La password deve contenere almeno una maiuscola, un numero, e un carattere speciale.");
            return "Register"; // Ritorna alla pagina con errore
        }

        try {
            // Se la password è valida, esegui la registrazione
            utenteService.registrati(utente);
            return "redirect:/utenti/login"; // Successo
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "Register"; // Ritorna alla pagina di registrazione in caso di errore
        }
    }

    // Metodo per la pagina di successo della registrazione
    @GetMapping("/registrazione-completa")
    public String registrationComplete() {
        return "Login"; // Questa pagina conferma la registrazione
    }
}
