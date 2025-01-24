package com.roguegames.domain.repository;

import com.roguegames.domain.entity.IndirizzoSpedizione;
import com.roguegames.domain.entity.IndirizzoSpedizioneId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IndirizzoSpedizioneRepository extends JpaRepository<IndirizzoSpedizione, IndirizzoSpedizioneId> {

    List<IndirizzoSpedizione> findByUtenteEmail(String email);
    Optional<IndirizzoSpedizione> findById(IndirizzoSpedizioneId id);
}
