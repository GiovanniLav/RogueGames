package com.roguegames.web.controller;

import com.roguegames.domain.entity.Ordine;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.OrdineRepository;
import com.roguegames.domain.service.CarrelloService;
import com.roguegames.domain.service.OrdineService;
import com.roguegames.web.controller.Item.CarrelloItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;




@Controller
public class ShowOrdiniController {

    @Autowired
    private OrdineService ordineService;

    @GetMapping("/Storico_Ordine")
    public String showOrdine(HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return "redirect:/login";
        }

        List <Ordine> ordini =ordineService.getOrdineUtente(utente.getEmail());
        model.addAttribute("ordini", ordini);
        model.addAttribute("utente", utente);

    return "OrdineUtente";
    }

}
