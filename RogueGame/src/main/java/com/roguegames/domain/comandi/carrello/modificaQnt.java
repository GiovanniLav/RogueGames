package com.roguegames.domain.comandi.carrello;

import com.roguegames.domain.comandi.Command;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.CarrelloService;

public class modificaQnt implements Command {
    private Prodotto prodotto;
    private PCarrello carrello;
    private CarrelloService carrelloService;
    private Utente utente;
    String qntProdotto;

    public modificaQnt(CarrelloService carrelloService, Prodotto prodotto, Utente utente, String qntProdotto) {
        this.prodotto = prodotto;
        this.carrelloService = carrelloService;
        this.utente=utente;
        this.qntProdotto = qntProdotto;
    }

    @Override
    public void execute(){
        carrelloService.modificaQnt(prodotto, utente, qntProdotto);
    }

}
