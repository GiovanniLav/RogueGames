package com.roguegames.domain.repository;

import com.roguegames.domain.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    // Trova un prodotto tramite il suo nome
    Prodotto findByNome(String nome);
}
