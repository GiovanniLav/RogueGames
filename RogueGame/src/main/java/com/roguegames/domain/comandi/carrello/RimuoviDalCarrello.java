package com.roguegames.domain.comandi.carrello;

import com.roguegames.domain.comandi.Command;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.CarrelloService;

public class RimuoviDalCarrello implements Command {
    private Prodotto prodotto;
    private PCarrello carrello;
    private CarrelloService carrelloService;
    private Utente utente;

    public RimuoviDalCarrello(CarrelloService carrelloService, Prodotto prodotto, Utente utente ) {
        this.carrelloService = carrelloService;
        this.prodotto = prodotto;
        this.utente = utente;
    }
    @Override
    public void execute(){
        carrelloService.rimuoviProdotto(prodotto, utente);
    }
}
