package com.roguegames.web.controller;

import com.roguegames.domain.entity.IndirizzoSpedizione;
import com.roguegames.domain.entity.IndirizzoSpedizioneId;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class IndirizzoSpedizioneController {

    @Autowired
    private UtenteService utenteService;

    @PostMapping("/indirizzo")
    public String showIndirizzi(Model model, HttpSession session){

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login"; // Se l'utente non è loggato, reindirizza al login
        }



        List<IndirizzoSpedizione> is = utenteService.getIndirizzoSpedizioni(utente);
        model.addAttribute("utente", utente);
        model.addAttribute("is", is); // Passa il modello vuoto
        return "test";  // La vista per aggiungere una carta
    }

    @PostMapping("/aggiungiIndirizzoSpedizione")
    public String aggiungiIndirizzoSpedizione(Model model, HttpSession session,
                                              @RequestParam ("provincia") String provincia,
                                              @RequestParam ("cap") int cap,
                                              @RequestParam ("via") String via,
                                              @RequestParam ("civico") String civico,
                                              @RequestParam ("citta") String citta){

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login"; // Se l'utente non è loggato, reindirizza al login
        }

        utenteService.aggiungiIndirizzoSpd(provincia, cap, via, civico, citta, utente);

        IndirizzoSpedizione is = new IndirizzoSpedizione();
        model.addAttribute("utente", utente);
        model.addAttribute("is", is); // Passa il modello vuoto
        return "Profilo";  // La vista per aggiungere una carta
    }

    @PostMapping("/rimuoviIndirizzo")
    public String rimuoviIndirizzo(Model model, HttpSession session,
                                   @RequestParam ("provincia") String provincia,
                                   @RequestParam ("cap") int cap,
                                   @RequestParam ("via") String via,
                                   @RequestParam ("civico") String civico,
                                   @RequestParam ("citta") String citta){

        Utente utente = (Utente) session.getAttribute("utente");
        System.out.println("lol: " + utente.getRuolo());
        if (utente == null) {
            return "redirect:/utenti/login";
        }

        IndirizzoSpedizioneId id = new IndirizzoSpedizioneId(provincia, cap, via, civico, citta, utente.getEmail());
        Optional<IndirizzoSpedizione> is = utenteService.getIndirizzoSpedizioneById(id);
        System.out.println("is: " + is.get().getId().getCap());
        if (is.isPresent()) {

            IndirizzoSpedizione is2 = is.get();
            utenteService.cancellaIndirizzoSpedizione(utente , is2);
        }
        model.addAttribute("utente", utente);
        return "test";
    }

}
