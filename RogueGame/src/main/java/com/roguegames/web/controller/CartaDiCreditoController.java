package com.roguegames.web.controller;

import com.roguegames.domain.entity.CartaDiCredito;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.CartaDiCreditoService;
import com.roguegames.domain.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/carte")
public class CartaDiCreditoController {

    @Autowired
    private CartaDiCreditoService cartaDiCreditoService;

    @Autowired
    private UtenteService utenteService;

    // Visualizza tutte le carte dell'utente
    @GetMapping
    public String mostraCarte(HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login"; // Se l'utente non è loggato, reindirizza al login
        }

        List<CartaDiCredito> carte = cartaDiCreditoService.getCarteByUtente(utente);

        model.addAttribute("utente", utente);
        model.addAttribute("carte", carte);
        model.addAttribute("carta", new CartaDiCredito()); // Oggetto vuoto per il form
        model.addAttribute("modifica", false); // Aggiungi la variabile `modifica`
        return "Carte";  // La vista che mostra le carte
    }


    // Visualizza il form per aggiungere una nuova carta
    @GetMapping("/aggiungi")
    public String aggiungiCarta(HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login"; // Se l'utente non è loggato, reindirizza al login
        }

        // Passa un oggetto CartaDiCredito vuoto al modello
        CartaDiCredito carta = new CartaDiCredito();
        model.addAttribute("utente", utente);
        model.addAttribute("carta", carta); // Passa il modello vuoto
        return "aggiungiCarta";  // La vista per aggiungere una carta
    }

    // Salva una nuova carta
    @PostMapping("/salva")
    public String salvaCarta(HttpSession session, @RequestParam String cif, @RequestParam String scadenza, @RequestParam String cvv) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login"; // Se l'utente non è loggato, reindirizza al login
        }

        CartaDiCredito carta = new CartaDiCredito(cif, scadenza, cvv, utente);
        cartaDiCreditoService.saveCarta(carta);

        return "redirect:/carte";  // Ritorna alla pagina delle carte
    }

    // Modifica una carta esistente
    @GetMapping("/modifica/{cif}")
    public String modificaCarta(@PathVariable String cif, Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login"; // Se l'utente non è loggato, reindirizza al login
        }

        CartaDiCredito carta = cartaDiCreditoService.getCartaByCif(cif);
        model.addAttribute("utente", utente);
        model.addAttribute("carta", carta); // Passa la carta da modificare
        model.addAttribute("modifica", true); // Imposta `modifica` a true
        return "Carte"; // Torna alla vista `carte`
    }


    // Salva le modifiche di una carta
    // Salva le modifiche di una carta
    @PostMapping("/aggiorna")
    public String aggiornaCarta(HttpSession session, @RequestParam String cif, @RequestParam String scadenza, @RequestParam String cvv) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login"; // Se l'utente non è loggato, reindirizza al login
        }

        CartaDiCredito carta = cartaDiCreditoService.getCartaByCif(cif);
        if (carta != null && carta.getUtente().equals(utente)) {
            carta.setScadenza(scadenza);
            carta.setCvv(cvv);
            cartaDiCreditoService.saveCarta(carta);
        }

        return "redirect:/carte"; // Ritorna alla pagina delle carte
    }


    // Elimina una carta
    @GetMapping("/elimina/{id}")
    public String eliminaCarta(@PathVariable("id") String cif, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/utenti/login"; // Se l'utente non è loggato, reindirizza al login
        }

        CartaDiCredito carta = cartaDiCreditoService.getCartaByCif(cif);
        if (carta != null && carta.getUtente().equals(utente)) {
            cartaDiCreditoService.deleteCarta(carta);
        }

        return "redirect:/carte";  // Ritorna alla pagina delle carte
    }

    @PostMapping("/salvaCartaOrdine")
    public ResponseEntity<?> salvaCartaOrd(Model model, HttpSession session, @RequestParam("cif") String cif, @RequestParam("scadenza") String scadenza, @RequestParam("cvv") String cvv) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Location", "/utenti/login").build(); // Se l'utente non è loggato, reindirizza al login
        }

        try {
            System.out.println("salvacartaordine");
            CartaDiCredito carta = new CartaDiCredito(cif, scadenza, cvv, utente);
            cartaDiCreditoService.saveCarta(carta);
            model.addAttribute("utente", utente);
            model.addAttribute("carta", carta);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/aggiornaCarte")
    @ResponseBody
    public Map<String, List<CartaDiCredito>> aggiornaCarte(HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");
        List<CartaDiCredito> carte = cartaDiCreditoService.getCarteByUtente(utente); // Recupera le carte dal database
        Map<String, List<CartaDiCredito>> response = new HashMap<>();
        response.put("carte", carte);
        return response;
    }
}
