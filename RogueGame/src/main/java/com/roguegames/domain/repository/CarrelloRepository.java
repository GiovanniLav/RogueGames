package com.roguegames.domain.repository;

import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.PCarrelloId;
import com.roguegames.domain.entity.Prodotto;
import com.roguegames.domain.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarrelloRepository extends JpaRepository<PCarrello, PCarrelloId> {
    void deleteById(PCarrelloId id);
    List<PCarrello> findAllByUtente(Utente utente);
    @Query(value = "SELECT * FROM pcarrello  WHERE Nome = :nome", nativeQuery = true)
    List<PCarrello> findAllByNome(@Param("nome")String nome);
}
