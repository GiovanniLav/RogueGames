package com.roguegames.domain.comandi;

import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.PreferitiService;

public class AggiungiCommand implements Command {

    private final PreferitiService preferitiService;
    private final String nome;
    private final Utente utente;  // Sostituire 'email' con 'utente'

    public AggiungiCommand(PreferitiService preferitiService, String nome, Utente utente) {
        this.preferitiService = preferitiService;
        this.nome = nome;
        this.utente = utente;
    }


    @Override
    public void execute() {
        preferitiService.aggiungiPreferito(nome, utente);  // Aggiungi il prodotto ai preferiti usando l'oggetto Utente
    }
}

