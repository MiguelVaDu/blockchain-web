package com.tads.blockchainweb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tads.blockchainweb.model.Transaction;
import com.tads.blockchainweb.model.UserDetail;
import com.tads.blockchainweb.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository txRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private BlockchainService blockchainService;

    //Crea una nueva transacción pendiente y gatilla el procesamiento de bloque.

    @Transactional
    public void crearYProcesarTransaccion(Long remitenteId, Long receptorId, double cantidad) {
        // Validar existencia de usuarios
        Optional<UserDetail> optRem = userService.findById(remitenteId);
        Optional<UserDetail> optRec = userService.findById(receptorId);
        if (optRem.isEmpty() || optRec.isEmpty()) {
            throw new IllegalArgumentException("Remitente o receptor no válido");
        }
        UserDetail remitente = optRem.get();
        // Validar saldo suficiente (asegurarse fuera del controlador si se desea)
        if (cantidad <= 0 || remitente.getSaldo() < cantidad) {
            throw new IllegalArgumentException("Saldo insuficiente o cantidad inválida");
        }
        // Crear transacción pendiente
        Transaction tx = Transaction.builder()
                .fecha(java.time.LocalDate.now())
                .hora(java.time.LocalTime.now())
                .id1(remitenteId)
                .id2(receptorId)
                .cantidad(cantidad)
                .estado("PENDIENTE")
                .build();
        txRepo.save(tx);
        // Procesar bloque si hay al menos 3 pendientes
        blockchainService.procesarPendientes();
    }
}