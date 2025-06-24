package com.tads.blockchainweb.repository;

import com.tads.blockchainweb.model.Ganancia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GananciaRepository extends JpaRepository<Ganancia, Long> {
    List<Ganancia> findByIdmin(Long idmin);
}