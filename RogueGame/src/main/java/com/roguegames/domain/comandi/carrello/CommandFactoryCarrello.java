package com.roguegames.domain.comandi.carrello;
import com.roguegames.domain.comandi.Command;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.CarrelloService;

public class CommandFactoryCarrello {
    private Prodotto prodotto;
    private PCarrello carrello;
    private CarrelloService carrelloService;
    private Utente utente;

    public Command createCommand(String commandType, Prodotto prodotto, PCarrello carrello) {

        switch (commandType) {
            case "aggiungi":
                return new AggiungiAlCarrello(carrelloService, prodotto, utente);
            default:
                throw new IllegalArgumentException("Comando non valido");
        }
    }
}

