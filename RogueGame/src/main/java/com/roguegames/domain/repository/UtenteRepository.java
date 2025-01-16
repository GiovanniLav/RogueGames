package com.roguegames.domain.repository;

import com.roguegames.domain.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {
    @Query("SELECT COUNT(u) FROM Utente u")
    int testConnection();


    Utente findByEmail(String email);
}
