package com.roguegames.web.controller;

import com.roguegames.domain.comandi.carrello.AggiungiAlCarrello;
import com.roguegames.domain.comandi.carrello.RimuoviDalCarrello;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.service.CarrelloService;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.service.ProdottoService;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.UtenteService;
import com.roguegames.web.controller.Item.CarrelloItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller

public class ShowCarrelloController {

    @Autowired
    private ProdottoService prodottoService;
    @Autowired
    private CarrelloService carrelloService;
    @Autowired
    private UtenteService utenteService;



    private List<CarrelloItem> getCarrelloItem(List<PCarrello> carrello) {
        List<CarrelloItem> carrelloItem = new ArrayList<CarrelloItem>();
        for (PCarrello c : carrello) {
            Prodotto p = prodottoService.findProdotto(c.getId().getNome());
            CarrelloItem ci =new CarrelloItem(p, c);
            carrelloItem.add(ci);
        }
        return carrelloItem;
    }

    @GetMapping("/carrello")
    public String ShowCarrello(Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");

        if(utente == null){
            return "redirect:/login";
        }

        List<PCarrello> carrello = carrelloService.getCarrello(utente);

        List<CarrelloItem> carrelloItem = getCarrelloItem(carrello);

        model.addAttribute("carrelloItem", carrelloItem);
        model.addAttribute("utente", utente);
        return "Carrello";
    }

    @GetMapping("/riepilogoAcquisto")
    public String ShowRiepilogo(Model model, HttpSession session) {
        Utente utente = (Utente) session.getAttribute("utente");

        if(utente == null){
            return "redirect:/login";
        }

        List<PCarrello> carrello = carrelloService.getCarrello(utente);

        List<CarrelloItem> carrelloItem = getCarrelloItem(carrello);

        double totale = 0;
        for (CarrelloItem item : carrelloItem) {
            totale += item.getPrezzo() * item.getCarrello().getQuantita();
        }

        model.addAttribute("totale", totale);
        model.addAttribute("carrelloItem", carrelloItem);
        model.addAttribute("utente", utente);
        return "RiepilogoOrdine";
    }

}
