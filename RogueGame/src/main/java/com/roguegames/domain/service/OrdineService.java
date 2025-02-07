package com.roguegames.domain.service;

import com.roguegames.domain.entity.Ordine;
import com.roguegames.domain.entity.Utente;
import com.roguegames.web.controller.Item.CarrelloItem;

import java.util.List;

public interface OrdineService {
    void processaOrdine(Utente utente, List<CarrelloItem> carrello1, double totale);

    List<Ordine> getOrdine();

    List<Ordine> getOrdineUtente(String email);
}
