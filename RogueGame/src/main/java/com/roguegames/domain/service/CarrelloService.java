package com.roguegames.domain.service;

import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.web.controller.Item.CarrelloItem;

import java.util.List;
import java.util.Optional;

public interface CarrelloService {
    public Optional<PCarrello> trovaElementoCarrello(String nomeProdotto, String emailCliente);
    public List<PCarrello> getPCarrello(List<CarrelloItem> carrello);
    public void aggiungiProdotto(Prodotto prodotto, Utente utente);
    public void rimuoviProdotto(Prodotto prodotto, Utente utente);
    public void rimuoviInteroCarrello (List <PCarrello> carrello, Utente utente);
    public void modificaQnt(Prodotto prodotto, Utente utente, String quantitaStr);
    public List<PCarrello> getCarrello(Utente utente);
    public List<PCarrello> getCarrelloNome(String nome);


    }
