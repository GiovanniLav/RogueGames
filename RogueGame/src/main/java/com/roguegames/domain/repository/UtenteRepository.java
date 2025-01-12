package com.roguegames.domain.repository;

import com.roguegames.domain.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {
    // Possiamo aggiungere metodi personalizzati per interrogare il DB, se necessario.
    // Per esempio:
    Utente findByEmail(String email);
}
