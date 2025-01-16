package com.roguegames.web.controller;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/catalogo")
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;


    @GetMapping("/prodotti")
    public String mostraProdotti(Model model) { // Modifica il tipo di ritorno e aggiungi Model
        List<Prodotto> prodotti = prodottoService.getAllProdotti();
        model.addAttribute("products", prodotti); // Aggiungi la lista al Model
        return "catalogo"; // Restituisci il nome del template Thymeleaf (prodotti.html)
    }
}
