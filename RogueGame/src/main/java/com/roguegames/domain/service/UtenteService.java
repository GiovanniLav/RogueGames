package com.roguegames.domain.service;

import com.roguegames.domain.entity.CartaDiCredito;
import com.roguegames.domain.entity.IndirizzoSpedizione;
import com.roguegames.domain.entity.IndirizzoSpedizioneId;
import com.roguegames.domain.entity.Utente;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface UtenteService {
    // Metodo per registrare un nuovo utente
    Utente registrati(@Valid Utente utente);

    Utente getUtenteByEmail(@NotNull String email);

    public String hashPassword(String password);

    Utente verificaCredenziali(String email, String password);

    void aggiornaUtente(Utente utente, String campo, String nuovoValore);

    boolean isDatabaseConnected();

    IndirizzoSpedizione aggiungiIndirizzoSpd(String provincia, Integer cap, String via, String civico, String citta, Utente utente);

    IndirizzoSpedizione modificaIndirizzoSpd(String provincia, Integer cap, String via, String civico, String citta, Utente utente, IndirizzoSpedizione is);

    List<IndirizzoSpedizione> getIndirizzoSpedizioni(Utente utente);

    Optional<IndirizzoSpedizione> getIndirizzoSpedizioneById(IndirizzoSpedizioneId id);

    void cancellaIndirizzoSpedizione(Utente utente, IndirizzoSpedizione indirizzoSpedizione);

    // Salva una carta di credito con validazioni
    void saveCarta(CartaDiCredito carta);

    // Ottieni le carte di credito di un utente
    List<CartaDiCredito> getCarteByUtente(Utente utente);

    // Ottieni una carta di credito tramite il CIF
    CartaDiCredito getCartaByCif(String cif);

    // Elimina una carta di credito
    void deleteCarta(CartaDiCredito carta);
}
