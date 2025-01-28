package com.roguegames.domain.repository;

import com.roguegames.domain.entity.Ordine;
import com.roguegames.domain.entity.OrdineId;
import com.roguegames.domain.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, OrdineId> {

    @Query(value = "SELECT * FROM ordine  WHERE Nome= :nome AND Email = :email AND Data = :data", nativeQuery = true)
    Ordine findByNomeAndEmailAndData(@Param("nome") String nome, @Param("email")String email, @Param("data") String data);

    @Query(value = "SELECT * FROM ordine  WHERE Email = :email", nativeQuery = true)
    List<Ordine> findByEmail(@Param("email")String email);
}
