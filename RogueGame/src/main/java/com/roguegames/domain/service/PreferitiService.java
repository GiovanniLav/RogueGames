package com.roguegames.domain.service;

import com.roguegames.domain.entity.Preferiti;
import com.roguegames.domain.entity.PreferitiId;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;  // Aggiungi l'importazione di Utente
import com.roguegames.domain.repository.PreferitiRepository;
import com.roguegames.domain.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferitiService {

    @Autowired
    private PreferitiRepository preferitiRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    // Metodo aggiornato per usare l'oggetto Utente
    public List<Preferiti> trovaPerUtente(Utente utente) {
        return preferitiRepository.findByUtente(utente);  // Cambia per passare l'utente invece dell'email
    }

    // Metodo aggiornato per usare l'oggetto Utente
    public void aggiungiPreferito(String nome, Utente utente) {
        PreferitiId id = new PreferitiId(nome, utente.getEmail());  // Usa l'email dell'utente
        Prodotto prodotto=prodottoRepository.findByNome(nome);
        Preferiti preferito = new Preferiti(id, prodotto.getImmagine());
        preferitiRepository.save(preferito);
    }

    // Metodo aggiornato per usare l'oggetto Utente
    public void rimuoviPreferito(String nome, Utente utente) {
        PreferitiId id = new PreferitiId(nome, utente.getEmail());  // Usa l'email dell'utente
        preferitiRepository.deleteById(id);
    }

    // Metodo aggiornato per usare l'oggetto Utente
    public boolean ePreferito(String nome, Utente utente) {
        return preferitiRepository.existsById(new PreferitiId(nome, utente.getEmail()));  // Usa l'email dell'utente
    }


}
