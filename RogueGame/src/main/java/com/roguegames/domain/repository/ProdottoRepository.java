package com.roguegames.domain.repository;


import com.roguegames.domain.entity.Ordine;
import com.roguegames.domain.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    Prodotto findByNome(String nome);

    @Query(value = "SELECT * FROM Prodotti WHERE rand() limit 6", nativeQuery = true)
    List<Prodotto> findRandom();

    List<Prodotto> findByPiattaforma(Prodotto.Piattaforma piattaforma);

    @Query(value = "SELECT * FROM Prodotti WHERE genere='Fantasy'", nativeQuery = true)
    List<Prodotto> findFantasy();

    @Query(value = "SELECT * FROM Prodotti WHERE genere='Console'", nativeQuery = true)
    List<Prodotto> findConsole();

    List<Prodotto> findAllByOrderByDataRilascioAsc();
    List<Prodotto> findAllByOrderByDataRilascioDesc();

}

