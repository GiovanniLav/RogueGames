package com.roguegames.web.controller;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.PreferitiService;
import com.roguegames.domain.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/catalogo") // Base path per tutte le operazioni relative al catalogo
public class CatalogoController {

    @Autowired
    private ProdottoService prodottoService;

    @Autowired
    private PreferitiService preferitiService;

    @GetMapping("/prodotti") // Gestisce sia utenti loggati che non loggati
    public String mostraProdotti(Model model, HttpSession session) {
        // Recupera l'utente dalla sessione
        Object utenteObject = session.getAttribute("utente");
        boolean utenteLoggato = (utenteObject != null);

        List<Prodotto> prodotti = prodottoService.getAllProdotti();

        if (utenteLoggato) {
            // Se l'utente è loggato, aggiungi i preferiti
            Utente utente = (Utente) utenteObject; // Cast dell'oggetto utente
            model.addAttribute("utente", utente);
            for (Prodotto prodotto : prodotti) {
                boolean isPreferito = preferitiService.ePreferito(prodotto.getNome(), utente);
                prodotto.setPreferito(isPreferito);
            }
        }

        model.addAttribute("products", prodotti);

        return "Catalogo"; // Ritorna alla pagina del catalogo
    }
    @GetMapping
    public String getCatalogo(@RequestParam(value = "search", required = false) String search, Model model, HttpSession session) {
        List<Prodotto> products = prodottoService.getAllProdotti();
        Utente utente = (Utente) session.getAttribute("utente");
        if (search != null && !search.isEmpty()) {
            products = products.stream()
                    .filter(product -> product.getNome().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("utente", utente);
        model.addAttribute("products", products);
        return "catalogo";
    }
    @GetMapping("/ordina")
    public String ordinaCatalogo(@RequestParam(name = "order", defaultValue = "asc") String order, Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");
        List<Prodotto> prodottiOrdinati = prodottoService.ordinaPerData(order);
        if(utente != null) {
            model.addAttribute("utente", utente);
            model.addAttribute("products", prodottiOrdinati);
        }
        model.addAttribute("products", prodottiOrdinati);
        return "catalogo"; // Nome della vista HTML
    }
}
