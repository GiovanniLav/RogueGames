package com.roguegames.web.controller.Item;

import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.PCarrello;
public class CarrelloItem {
    private Prodotto prodotto;
    private PCarrello carrello;

    public CarrelloItem(Prodotto prodotto, PCarrello carrello) {
        this.prodotto = prodotto;
        this.carrello = carrello;
    }


    public PCarrello getCarrello() {
        return carrello;
    }

    public String getImmagine() {
        return prodotto.getImmagine();
    }

    public double getPrezzo() {return prodotto.getPrezzo();}
}
