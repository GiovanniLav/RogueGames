package com.roguegames.domain.comandi.carrello;

import com.roguegames.domain.comandi.Command;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.CarrelloService;

public class AggiungiAlCarrello implements Command {
    private Prodotto prodotto;
    private PCarrello carrello;
    private CarrelloService carrelloService;
    private Utente utente;
    public AggiungiAlCarrello(CarrelloService carrelloService, Prodotto prodotto, Utente utente) {
        this.prodotto = prodotto;
        this.carrelloService = carrelloService;
        this.utente=utente;
    }

    @Override
    public void execute(){
        carrelloService.aggiungiProdotto(prodotto, utente);
    }
}
