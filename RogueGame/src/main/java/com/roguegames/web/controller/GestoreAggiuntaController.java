package com.roguegames.web.controller;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.GestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
@RestController
@RequestMapping("/utenti")
public class GestoreAggiuntaController {

    @Autowired
    private GestoreService productService;

    @PostMapping("/prodotti/aggiungi")
    public ResponseEntity<?> aggiungiProdotto(@ModelAttribute Prodotto prodotto, HttpSession session, Model model) {
        Utente utente = (Utente) session.getAttribute("utente");
        try {

            if (utente == null) {
                return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
            }

            if(!utente.getRuolo().equals("gestore")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            // Verifica se il prodotto esiste già
            Prodotto prodottoEsistente = productService.getProductByName(prodotto.getNome());
            if (prodottoEsistente != null) {
                model.addAttribute("utente", utente);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Ricarica la pagina con l'errore
            }
            if(prodotto.getVideo().equals("")){
                prodotto.setVideo(null);
            }
            productService.saveProduct(prodotto); // Salva il nuovo prodotto
            model.addAttribute("utente", utente);
            return ResponseEntity.ok().build();
        }  catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
