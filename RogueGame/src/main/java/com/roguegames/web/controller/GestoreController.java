package com.roguegames.web.controller;

import com.roguegames.domain.entity.Ordine;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.GestoreService;
import com.roguegames.domain.service.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/utenti")
public class GestoreController {

    @Autowired
    private GestoreService productService;

    @Autowired
    private OrdineService ordineService;

    // Visualizza la lista di tutti i prodotti
    @GetMapping("/prodotti")
    public String mostraProdotti(Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null && !utente.getRuolo().equals("gestore")) {
            return "redirect:/login";
        }
        List<Prodotto> prodotti = productService.getAllProducts();// Recupera tutti i prodotti dal servizio
        model.addAttribute("utente", utente);
        model.addAttribute("prodotti", prodotti); // Aggiunge la lista di prodotti al modello
        return "prodotti"; // Restituisce la pagina 'utenti/prodotti.html'
    }

    // Gestisce l'aggiunta di un prodotto
    @PostMapping("/prodotti/aggiungi")
    public String aggiungiProdotto(@ModelAttribute Prodotto prodotto, HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null && !utente.getRuolo().equals("gestore")) {
            return "redirect:/login";
        }
        productService.saveProduct(prodotto); // Salva il nuovo prodotto
        model.addAttribute("utente", utente);
        return "redirect:/utenti/prodotti"; // Ricarica la pagina con i prodotti
    }

    // Gestisce la modifica di un prodotto
    @GetMapping("/prodotti/modifica/{nome}")
    public String modificaProdottoForm(@PathVariable String nome, Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null && !utente.getRuolo().equals("gestore")) {
            return "redirect:/login";
        }
        model.addAttribute("utente", utente);
        Prodotto prodotto = productService.getProductByName(nome); // Ottieni il prodotto da modificare
        model.addAttribute("prodotto", prodotto); // Aggiungi il prodotto al modello
        return "modificaProdotto"; // La pagina per modificare il prodotto
    }

    @PostMapping("/prodotti/modifica/{nome}")
    public String modificaProdotto(@PathVariable String nome, @ModelAttribute Prodotto prodotto, HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null && !utente.getRuolo().equals("gestore")) {
            return "redirect:/login";
        }
        prodotto.setNome(nome); // Mantieni il nome originale per l'update
        productService.updateProduct(prodotto); // Modifica il prodotto
        model.addAttribute("utente", utente);
        return "redirect:/utenti/prodotti"; // Ricarica la pagina
    }

    // Gestisce l'eliminazione di un prodotto
    @PostMapping("/prodotti/elimina/{nome}")
    public String eliminaProdotto(@PathVariable String nome, HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null && !utente.getRuolo().equals("gestore")) {
            return "redirect:/login";
        }
        model.addAttribute("utente", utente);
        productService.deleteProduct(nome); // Elimina il prodotto
        return "redirect:/utenti/prodotti"; // Ricarica la pagina con i prodotti
    }

    @GetMapping("/prodotti/{nome}")
    @ResponseBody
    public Prodotto getProdottoByNome(@PathVariable String nome, HttpSession session) {
        Prodotto prodotto = productService.getProductByName(nome);
        if (prodotto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prodotto non trovato");
        }
        return prodotto;
    }

    @GetMapping("/ordini")
    public String getOrdine(Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null && !utente.getRuolo().equals("gestore")) {
            return "redirect:/login";
        }
        List <Ordine> ordine = ordineService.getOrdine();
        model.addAttribute("ordini", ordine);
        model.addAttribute("utente", utente);
        return "StoricoOrdineGestore";
    }

}
