package com.tads.blockchainweb.repository;

import com.tads.blockchainweb.model.BloqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BloqueRepository extends JpaRepository<BloqueEntity, Integer> {
    Optional<BloqueEntity> findTopByOrderByIdDesc();
}