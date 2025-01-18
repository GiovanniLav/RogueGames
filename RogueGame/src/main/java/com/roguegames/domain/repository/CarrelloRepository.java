package com.roguegames.domain.repository;

import com.roguegames.domain.entity.PCarrello;
import com.roguegames.domain.entity.PCarrelloId;
import com.roguegames.domain.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrelloRepository extends JpaRepository<PCarrello, PCarrelloId> {
    PCarrello deletePCarrelloById(PCarrelloId id);
}
