package com.roguegames.web.controller;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class HomeController {

    @Autowired
    private ProdottoService prodottoService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        // Ottieni 6 prodotti random
        List<Prodotto> prodotti = prodottoService.get6RandomProdotto();
        List<Prodotto> fantasyProducts= prodottoService.getFantasy();
        List<Prodotto> consoleProducts= prodottoService.getConsole();
        model.addAttribute("fantasyProducts", fantasyProducts);
        model.addAttribute("consoleProducts", consoleProducts);
        model.addAttribute("prodotti", prodotti);
        return "Home";
    }

    @GetMapping("/filteredCatalogo")
    public String showFilteredCatalogo(@RequestParam("piattaforma") String piattaforma, Model model , HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");
        Prodotto.Piattaforma plat= Prodotto.Piattaforma.valueOf(piattaforma);
        List<Prodotto> prodotti= prodottoService.filteredCatalogo(plat);
        model.addAttribute("utente", utente);
        model.addAttribute("products", prodotti);
        return "Catalogo";
    }
}
