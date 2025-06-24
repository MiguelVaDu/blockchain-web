package com.tads.blockchainweb.repository;

import com.tads.blockchainweb.model.Miner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MinerRepository extends JpaRepository<Miner, Long> { }

