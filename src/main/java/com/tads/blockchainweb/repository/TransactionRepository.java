package com.tads.blockchainweb.repository;

import com.tads.blockchainweb.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByEstadoOrderByFechaAscHoraAsc(String estado);
    List<Transaction> findById1OrId2OrderByFechaAscHoraAsc(Long id1, Long id2);
}

