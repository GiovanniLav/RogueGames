package com.roguegames.domain.repository;


import com.roguegames.domain.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GestoreRepository extends JpaRepository<Prodotto, String> {
    Optional<Prodotto> findById(String nome);
    void deleteById(String nome);
}
