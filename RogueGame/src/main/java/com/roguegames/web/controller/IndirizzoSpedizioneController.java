package com.roguegames.web.controller;

import com.roguegames.domain.entity.IndirizzoSpedizione;
import com.roguegames.domain.entity.IndirizzoSpedizioneId;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.UtenteService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;

@Controller
public class IndirizzoSpedizioneController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/indirizzo")
    public String showIndirizzi(Model model, HttpSession session){

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login"; // Se l'utente non è loggato, reindirizza al login
        }


        List<IndirizzoSpedizione> is = utenteService.getIndirizzoSpedizioni(utente);
        model.addAttribute("utente", utente);
        model.addAttribute("is", is);
        model.addAttribute("modifica", false);
        return "Indirizzo";  // La vista per aggiungere una carta
    }

    @PostMapping("/aggiungiIndirizzoSpedizione")
    public ResponseEntity<?> aggiungiIndirizzoSpedizione(Model model, HttpSession session,
                                                              @RequestParam ("provincia") String provincia,
                                                              @RequestParam ("cap") int cap,
                                                              @RequestParam ("via") String via,
                                                              @RequestParam ("civico") String civico,
                                                              @RequestParam ("citta") String citta){

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Location", "/utenti/login").build(); // Se l'utente non è loggato, reindirizza al login
        }

        try {
            IndirizzoSpedizione is = utenteService.aggiungiIndirizzoSpd(provincia, cap, via, civico, citta, utente);
            model.addAttribute("utente", utente);
            model.addAttribute("is", is);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }  // La vista per aggiungere una carta
    }

    @PostMapping("/modificaIndirizzoSpedizione")
    public ResponseEntity<?> modificaIndirizzoSpedizione(Model model, HttpSession session,
                                                                 @RequestParam ("provincia") String provincia,
                                                                 @RequestParam ("cap") int cap,
                                                                 @RequestParam ("via") String via,
                                                                 @RequestParam ("civico") String civico,
                                                                 @RequestParam ("citta") String citta,
                                                                 @RequestParam ("provinciamod") String provinciamod,
                                                                 @RequestParam ("capmod") int capmod,
                                                                 @RequestParam ("viamod") String viamod,
                                                                 @RequestParam ("civicomod") String civicomod,
                                                                 @RequestParam ("cittamod") String cittamod) {

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Location", "/utenti/login").build(); // Se l'utente non è loggato, reindirizza al login
        }


        try {
            IndirizzoSpedizioneId id = new IndirizzoSpedizioneId(provinciamod, capmod, viamod, civicomod, cittamod, utente.getEmail());
            Optional<IndirizzoSpedizione> iso = utenteService.getIndirizzoSpedizioneById(id);
            if (iso.isPresent()) {
                IndirizzoSpedizione is = iso.get();
                IndirizzoSpedizione is2 = utenteService.modificaIndirizzoSpd(provincia, cap, via, civico, citta, utente, is);
                model.addAttribute("utente", utente);
                model.addAttribute("is", is2);
                model.addAttribute("modifica", false);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (UtenteService.IndirizzoNonDisponibile e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/rimuoviIndirizzo")
    public String rimuoviIndirizzo(Model model, HttpSession session,
                                   @RequestParam ("provincia") String provincia,
                                   @RequestParam ("cap") int cap,
                                   @RequestParam ("via") String via,
                                   @RequestParam ("civico") String civico,
                                   @RequestParam ("citta") String citta){

        Utente utente = (Utente) session.getAttribute("utente");
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
        model.addAttribute("modifica", false);
        return "Indirizzo";
    }

    @PostMapping("/modificaIndirizzo")
    public String modifica(Model model, HttpSession session,
                                                       //@RequestParam Map<String, String> requestParams
                                        @RequestParam("provincia") String provincia,
                                        @RequestParam("cap") String capp,
                                        @RequestParam("via") String via,
                                        @RequestParam("civico") String civico,
                                        @RequestParam("citta") String citta) {

        //String provincia = requestParams.get("provincia");
        int cap = Integer.parseInt(capp);
        /*String via = requestParams.get("via");
        String civico = requestParams.get("civico");
        String citta = requestParams.get("citta");*/

        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            //return Map.of("success", false, "redirectUrl", "/utenti/login");
            return "redirect:/utenti/login";
        }

        IndirizzoSpedizioneId id = new IndirizzoSpedizioneId(provincia, cap, via, civico, citta, utente.getEmail());
        Optional<IndirizzoSpedizione> is = utenteService.getIndirizzoSpedizioneById(id);
        if (is.isPresent()) {
            IndirizzoSpedizione is2 = is.get();
            System.out.println("is2: " + is2.getId().getCap());
            model.addAttribute("is", is2);
            model.addAttribute("modifica", true);
            model.addAttribute("utente", utente);
            //return Map.of("success", true, "redirectUrl", "Indirizzo");
            return "Indirizzo";
        }

        //return Map.of("success", false, "message", "Errore durante l'aggiornamento dell'indirizzo");
        model.addAttribute("modifica", false);
        model.addAttribute("utente", utente);
        return "Indirizzo";
    }

    @GetMapping("/aggiornaIndirizzi")
    public ResponseEntity<?> aggiornaIndirizzi(HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");

        if (utente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("errore", "Utente non autenticato"));
        }

        List<IndirizzoSpedizione> indirizzi = utenteService.getIndirizzoSpedizioni(utente);

        return ResponseEntity.ok(Collections.singletonMap("indirizzi", indirizzi));
    }

}
