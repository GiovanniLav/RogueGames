package com.roguegames.domain.comandi;

import com.roguegames.domain.entity.Utente;
import com.roguegames.domain.service.PreferitiService;

public class RimuoviCommand implements Command {

    private final PreferitiService preferitiService;
    private final String nome;
    private final Utente utente;  // Sostituire 'email' con 'utente'

    public RimuoviCommand(PreferitiService preferitiService, String nome, Utente utente) {
        this.preferitiService = preferitiService;
        this.nome = nome;
        this.utente = utente;
    }

    @Override
    public void execute() {
        preferitiService.rimuoviPreferito(nome, utente);  // Rimuovi il prodotto dai preferiti usando l'oggetto Utente
    }
}
