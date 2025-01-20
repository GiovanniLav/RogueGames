package com.roguegames.web.controller;

import com.roguegames.domain.entity.Ordine;
import com.roguegames.domain.service.OrdineService;
import com.roguegames.web.controller.Item.CarrelloItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class OrdineController {

    @Autowired
    private OrdineService ordineService;

    @PostMapping("/aggiungiOrdine")
    public String aggiungiOrdine(HttpSession session, @ModelAttribute("carrello") CarrelloItem carrello, double total) {


        return "Ordine";
    }
}
