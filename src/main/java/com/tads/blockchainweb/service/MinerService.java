package com.tads.blockchainweb.service;

import com.tads.blockchainweb.model.Ganancia;
import com.tads.blockchainweb.repository.GananciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MinerService {
    @Autowired
    private GananciaRepository gananciaRepo;
    public List<Ganancia> obtenerPorMiner(Long minerId) {
        return gananciaRepo.findByIdmin(minerId);
    }
}