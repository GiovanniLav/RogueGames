package com.roguegames.domain.repository;

import com.roguegames.domain.entity.CartaDiCredito;
import com.roguegames.domain.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartaDiCreditoRepository extends JpaRepository<CartaDiCredito, String> {
    List<CartaDiCredito> findByUtente(Utente utente);  // Trova tutte le carte associate a un utente
}
