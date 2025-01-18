package com.roguegames.domain.repository;
import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.PCarrelloId;
import com.roguegames.domain.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PCarrelloRepository extends JpaRepository<PCarrello, PCarrelloId>{
    Optional<PCarrello> findById(PCarrelloId id);
}
