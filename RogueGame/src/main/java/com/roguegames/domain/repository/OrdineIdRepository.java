package com.roguegames.domain.repository;

import com.roguegames.domain.entity.Ordine;
import com.roguegames.domain.entity.OrdineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface OrdineIdRepository extends JpaRepository <Ordine, OrdineId>{
    Optional<Ordine> findById(OrdineId id);

    @Query(value = "SELECT * FROM ordine  WHERE Nome= :nome AND Email = :email AND Data = :data", nativeQuery = true)
    Optional<Ordine> findByNomeAndEmailAndData(@Param("nome") String nome, @Param("email")String email, @Param("data") String data);
}
