package com.roguegames.web.controller;

import com.roguegames.domain.entity.Prodotto;
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

    /*@GetMapping("/")
    public String home(Model model) {
        return "Home"; // Restituisce la vista "index.html"
    }*/

    @GetMapping("/login")
    public String login() {
        return "Login"; // Restituisce la vista "login.html"
    }

    //Mostra 6 Prodotti Random in fondo alla Home
    @GetMapping("/")
    public String showHomePage(Model model) {
        // Ottieni 6 prodotti random
        List<Prodotto> prodotti = prodottoService.get6RandomProdotto();
        model.addAttribute("prodotti", prodotti); // Aggiungi i prodotti al modello
        return "Home";
    }

    @GetMapping("/filteredCatalogo")
    public String showFilteredCatalogo(@RequestParam("piattaforma") String piattaforma, Model model) {
        Prodotto.Piattaforma plat= Prodotto.Piattaforma.valueOf(piattaforma);
        List<Prodotto> prodotti= prodottoService.filteredCatalogo(plat);
        model.addAttribute("products", prodotti);
        return "Catalogo";
    }
}
