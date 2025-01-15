package com.roguegames.web.controller;

import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public String showRegistrationForm(Model model) {
        model.addAttribute("utente", new Utente());
        return "Register"; // Restituisce la pagina Register.html
    }

    // Metodo per gestire la registrazione dell'utente
    @PostMapping("/registrati")
    public String registerUtente(@Valid @ModelAttribute("utente") Utente utente, BindingResult result, Model model) {
        // Se ci sono errori nel form, torna alla pagina di registrazione
        if (result.hasErrors()) {
            return "Register"; // Se ci sono errori, ritorna alla pagina di registrazione
        }

        try {
            utenteService.registrati(utente); // Salva l'utente
            return "redirect:/utenti/login"; // Successo, reindirizza a una pagina di successo
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "Register"; // Se c'è un errore (es. email già registrata), ritorna al form
        }
    }

    // Metodo per la pagina di successo della registrazione
    @GetMapping("/registrazione-completa")
    public String registrationComplete() {
        return "Login"; // Questa pagina conferma la registrazione
    }

}
