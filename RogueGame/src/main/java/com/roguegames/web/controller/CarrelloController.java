package com.roguegames.web.controller;
import com.roguegames.domain.comandi.carrello.AggiungiAlCarrello;
import com.roguegames.domain.comandi.carrello.modificaQnt;
import com.roguegames.domain.comandi.carrello.RimuoviDalCarrello;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.service.CarrelloService;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.service.CarrelloServiceImp;
import com.roguegames.domain.service.ProdottoService;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
public class CarrelloController {
    @Autowired
    private ProdottoService prodottoService;
    @Autowired
    private CarrelloService carrelloService;
    @Autowired
    private UtenteService utenteService;

    @PostMapping("/aggiungi/{nome}")
    public ResponseEntity<?> AgggiungiAlCarrello(@PathVariable String nome, HttpSession session) {
        PCarrello carrello;
        Utente utente = (Utente) session.getAttribute("utente");
        if (utente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if(utente.getRuolo().equals("gestore")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Prodotto prodotto= prodottoService.findProdotto(nome);

        System.out.println("prodotto trovato"+prodotto.getNome());

        if (prodotto == null) {
            return ResponseEntity.badRequest().body("Prodotto non presente");
        }
        try{
            AggiungiAlCarrello command= new AggiungiAlCarrello(carrelloService, prodotto, utente);
            command.execute();
            return ResponseEntity.ok().build();
        }catch (CarrelloServiceImp.QuantitaNonDisponibileException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
         }
    }

    @PostMapping("/rimuovi/{nome}")
    public ResponseEntity<?> RimuoviAlCarrello(@PathVariable String nome, HttpSession session) {
        PCarrello carrello;
        Utente utente = (Utente) session.getAttribute("utente");
        if(utente == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Prodotto prodotto = new Prodotto();
        prodotto.setNome(nome);
        try{
           RimuoviDalCarrello command= new RimuoviDalCarrello(carrelloService, prodotto, utente);
           command.execute();
           return ResponseEntity.ok().build();
        }catch (CarrelloServiceImp.QuantitaNonDisponibileException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/aumentaQnt")
    public ResponseEntity<?> aumentaQuantita(
            @RequestParam("nomeProdotto") String nome,
            @RequestParam("quantita") String quantitaStr,
            HttpSession session) {

        Utente utente = (Utente) session.getAttribute("utente");
        if(utente == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Location", "/utenti/login").build();
        }

        Prodotto prodotto = new Prodotto();
        prodotto.setNome(nome);

        try{
            modificaQnt command= new modificaQnt(carrelloService, prodotto, utente, quantitaStr);
            command.execute();
            return ResponseEntity.ok().build();

        }catch (CarrelloServiceImp.QuantitaNonDisponibileException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server: " + e.getMessage());
        }
    }
}
