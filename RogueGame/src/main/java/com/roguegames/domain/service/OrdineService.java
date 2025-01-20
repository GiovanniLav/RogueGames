package com.roguegames.domain.service;

import com.roguegames.domain.entity.Ordine;
import com.roguegames.domain.entity.OrdineId;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.repository.OrdineRepository;
import com.roguegames.web.controller.Item.CarrelloItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrdineService {

    @Autowired
    OrdineRepository ordineRepository;

    @Transactional
    public void aggiungiOrdine(Utente utente, List<CarrelloItem> carrello, double totale) {

        if (carrello == null || carrello.isEmpty()) {
            throw new IllegalArgumentException("Il carrello non può essere vuoto.");
        }

        for (CarrelloItem item : carrello) {

            double prz = item.getPrezzo();
            int qnt = item.getCarrello().getQuantita();
            double prezzoQnt = prz*qnt;

            OrdineId id= new OrdineId(null, item.getCarrello().getId().getNome(), utente.getEmail());

            Ordine ordine = new Ordine(id, prz, totale, qnt, false, LocalDate.now(), utente);
        }
    }

}
