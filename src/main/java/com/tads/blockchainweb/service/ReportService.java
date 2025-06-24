package com.tads.blockchainweb.service;

import com.tads.blockchainweb.model.Ganancia;
import com.tads.blockchainweb.model.Miner;
import com.tads.blockchainweb.model.Transaction;
import com.tads.blockchainweb.repository.GananciaRepository;
import com.tads.blockchainweb.repository.MinerRepository;
import com.tads.blockchainweb.repository.TransactionRepository;
import com.tads.blockchainweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private TransactionRepository txRepo;
    @Autowired private GananciaRepository ganRepo;
    @Autowired private MinerRepository minerRepo;
    @Autowired private UserRepository userRepo;

    public List<Transaction> todasTransacciones() {
        return txRepo.findAll();
    }
    public Map<Miner, Double> reporteGananciasPorMiner() {
        Map<Miner, Double> res = new HashMap<>();
        for (Miner m: minerRepo.findAll()) {
            double total = ganRepo.findByIdmin(m.getId())
                    .stream().mapToDouble(Ganancia::getGanancia).sum();
            res.put(m, total);
        }
        return res;
    }
}

