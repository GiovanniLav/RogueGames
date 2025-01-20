package com.roguegames.web.controller;


import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.ProdottoRepository;
import com.roguegames.domain.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/DettagliProdotto")
public class DettagliProdottoController {
    @Autowired
    private ProdottoService prodottoService;

    @GetMapping("/prodotto/{nome}")
    public String getDettagliProdotto(Model model,@PathVariable String nome, HttpSession session) {

        Utente utente = (Utente) session.getAttribute("utente");

            Prodotto prodotto = prodottoService.findProdotto(nome);
            model.addAttribute("product", prodotto);
            model.addAttribute("utente", utente);

            return "DettagliProdotto";
        }
    }
    /*
    @GetMapping("/prod/{nome}")

    public String getDetProd(Model model,@PathVariable String nome) {
        Prodotto prodotto = prodottoService.findProdotto(nome);
        model.addAttribute("product", prodotto);


        return "DettagliProdotto";
    }

     */


