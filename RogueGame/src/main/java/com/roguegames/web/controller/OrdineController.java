package com.roguegames.web.controller;

import com.roguegames.domain.entity.Ordine;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.OrdineRepository;
import com.roguegames.domain.service.CarrelloService;
import com.roguegames.domain.service.OrdineService;
import com.roguegames.domain.service.ProdottoService;
import com.roguegames.web.controller.Item.CarrelloItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;

@RestController
public class OrdineController {


    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private OrdineService ordineService;

    @Autowired
    private ProdottoService prodottoService;

    @PostMapping("/aggiungiOrdine")
    public ResponseEntity<Object> aggiungiOrdine(Model model, HttpSession session, double totale) throws ParseException {

        Utente utente = (Utente) session.getAttribute("utente");

        List<CarrelloItem> carrello = (List<CarrelloItem>) session.getAttribute("carrelloItem");
        List<CarrelloItem> carr = carrello;

        if(utente == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Location", "/utenti/login").build();
        }

        if(utente.getRuolo().equals("gestore")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (!carrello.isEmpty() && carrello != null) {
            ordineService.processaOrdine(utente, carrello, totale);
        }

       for (CarrelloItem carrelloItem : carrello) {
           Prodotto prodotto = prodottoService.findProdotto(carrelloItem.getCarrello().getProdotto().getNome());
           if (prodotto != null) {
               int qnt=carrelloItem.getCarrello().getQuantita();
               prodottoService.updateProdottoQnt(prodotto, qnt);
           }
       }


        List< PCarrello> cart = carrelloService.getPCarrello(carrello);

        carrelloService.rimuoviInteroCarrello(cart,utente);

        session.removeAttribute("carrelloItem");
        return ResponseEntity.ok("Ordine effettuato con successo");

    }
}
