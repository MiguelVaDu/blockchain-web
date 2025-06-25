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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired private GananciaRepository gananciaRepo;
    @Autowired private MinerRepository minerRepo;

    public List<MineroReporte> reportePorMiner() {
        List<MineroReporte> lista = new ArrayList<>();
        for (Miner m : minerRepo.findAll()) {
            List<Ganancia> gs = gananciaRepo.findByIdmin(m.getId());
            double total = gs.stream().mapToDouble(Ganancia::getGanancia).sum();
            int bloques = gs.size();
            lista.add(new MineroReporte(m.getNombre(), m.getDni(), bloques, total));
        }
        return lista;
    }

    public static class MineroReporte {
        private String nombre;
        private String dni;
        private int bloques;
        private double ganancia;

        public MineroReporte(String nombre, String dni, int bloques, double ganancia) {
            this.nombre = nombre;
            this.dni = dni;
            this.bloques = bloques;
            this.ganancia = ganancia;
        }
        // getters
        public String getNombre() { return nombre; }
        public String getDni() { return dni; }
        public int getBloques() { return bloques; }
        public double getGanancia() { return ganancia; }
    }

}

