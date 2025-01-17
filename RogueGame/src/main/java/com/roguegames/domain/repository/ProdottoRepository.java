package com.roguegames.domain.repository;


import com.roguegames.domain.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

}