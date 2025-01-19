package com.roguegames.web.controller;


import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.repository.ProdottoRepository;
import com.roguegames.domain.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/DettagliProdotto")
public class DettagliProdottoController {
    @Autowired
    private ProdottoService prodottoService;

    @GetMapping("/prodotto/{nome}")
    public String getDettagliProdotto(Model model,@PathVariable String nome) {
            Prodotto prodotto = prodottoService.findProdotto(nome);
            model.addAttribute("product", prodotto);


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


