package com.roguegames.domain.repository;

import com.roguegames.domain.entity.IndirizzoSpedizione;
import com.roguegames.domain.entity.IndirizzoSpedizioneId;
import com.roguegames.domain.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IndirizzoSpedizioneIdRepository extends JpaRepository<IndirizzoSpedizione, IndirizzoSpedizioneId> {
    //public List<IndirizzoSpedizioneId> findByUtente(Utente utente);
}
