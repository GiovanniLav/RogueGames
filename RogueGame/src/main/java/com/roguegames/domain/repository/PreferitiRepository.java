package com.roguegames.domain.repository;

import com.roguegames.domain.entity.Preferiti;
import com.roguegames.domain.entity.PreferitiId;
import com.roguegames.domain.entity.Utente; // Aggiungi importazione per Utente
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferitiRepository extends JpaRepository<Preferiti, PreferitiId> {
    List<Preferiti> findByUtente(Utente utente);  // Modifica per usare Utente invece di email
}
