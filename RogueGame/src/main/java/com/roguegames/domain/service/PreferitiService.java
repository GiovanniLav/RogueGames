package com.roguegames.domain.service;

import com.roguegames.domain.entity.Preferiti;
import com.roguegames.domain.entity.Utente;

import java.util.List;

public interface PreferitiService {
    // Metodo aggiornato per usare l'oggetto Utente
    List<Preferiti> trovaPerUtente(Utente utente);

    // Metodo aggiornato per usare l'oggetto Utente
    void aggiungiPreferito(String nome, Utente utente);

    // Metodo aggiornato per usare l'oggetto Utente
    void rimuoviPreferito(String nome, Utente utente);

    // Metodo aggiornato per usare l'oggetto Utente
    boolean ePreferito(String nome, Utente utente);
}
