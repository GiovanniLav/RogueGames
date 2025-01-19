package com.roguegames.web.controller;

import com.roguegames.domain.comandi.carrello.AggiungiAlCarrello;
import com.roguegames.domain.comandi.carrello.RimuoviDalCarrello;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.service.CarrelloService;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.service.ProdottoService;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller

public class ShowCarrelloController {

    @Autowired
    private ProdottoService prodottoService;
    @Autowired
    private CarrelloService carrelloService;
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/carrello")
    public String ShowCarrello(Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");

        if(utente == null){
            return "redirect:/login";
        }

        List<PCarrello> carrello = carrelloService.getCarrello(utente);

        model.addAttribute("carrello", carrello);
        model.addAttribute("utente", utente);
        return "Carrello";
    }

}
