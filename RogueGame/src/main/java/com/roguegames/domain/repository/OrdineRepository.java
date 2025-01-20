package com.roguegames.domain.repository;

import com.roguegames.domain.entity.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdineRepository extends JpaRepository<Ordine, Integer> {
}
