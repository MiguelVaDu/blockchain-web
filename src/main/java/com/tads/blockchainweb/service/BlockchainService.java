package com.tads.blockchainweb.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tads.blockchainweb.block.BlockModel;
import com.tads.blockchainweb.block.Contants;
import com.tads.blockchainweb.block.MinerAdapter;
import com.tads.blockchainweb.model.BloqueEntity;
import com.tads.blockchainweb.model.Ganancia;
import com.tads.blockchainweb.model.UserDetail;
import com.tads.blockchainweb.model.Transaction;
import com.tads.blockchainweb.model.Miner;
import com.tads.blockchainweb.repository.BloqueRepository;
import com.tads.blockchainweb.repository.GananciaRepository;
import com.tads.blockchainweb.repository.MinerRepository;
import com.tads.blockchainweb.repository.TransactionRepository;

@Service
public class BlockchainService {

    @Autowired
    private TransactionRepository txRepo;
    @Autowired
    private BloqueRepository bloqueRepo;
    @Autowired
    private MinerRepository minerRepo;
    @Autowired
    private GananciaRepository gananciaRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    private Random random = new Random();

    /**
     * Procesa transacciones pendientes en bloques de 3.
     * Se invoca desde TransactionService.
     */
    @Transactional
    public void procesarPendientes() {
        List<Transaction> pendientes = txRepo.findByEstadoOrderByFechaAscHoraAsc("PENDIENTE");
        if (pendientes.size() >= 3) {
            // Tomar las primeras 3 (ordenadas por fecha/hora)
            List<Transaction> trio = pendientes.subList(0, 3);
            // Construir datos concatenados de transacciones
            String datos = trio.stream()
                    .map(t -> t.getId1() + "->" + t.getId2() + ":" + t.getCantidad())
                    .collect(Collectors.joining("|"));
            // Determinar siguiente ID de bloque
            int nextId = bloqueRepo.findTopByOrderByIdDesc()
                    .map(be -> be.getId() + 1)
                    .orElse(0);
            // Obtener hash anterior
            String prevHash = bloqueRepo.findTopByOrderByIdDesc()
                    .map(BloqueEntity::getHash)
                    .orElse(Contants.GENESIS_PREV_HASH);
            // Minar bloque
            BlockModel blockModel = new BlockModel(nextId, datos, prevHash);
            // Seleccionar minero aleatorio
            List<Miner> mineros = minerRepo.findAll();
            if (mineros.isEmpty()) {
                throw new IllegalStateException("No hay mineros configurados");
            }
            Miner elegido = mineros.get(random.nextInt(mineros.size()));
            MinerAdapter minerAdapter = new MinerAdapter();
            minerAdapter.mine(blockModel);
            // Guardar bloque en BD
            BloqueEntity be = BloqueEntity.builder()
                    .id(blockModel.getId())
                    .transacciones(datos)
                    .previousHash(blockModel.getPreviousHash())
                    .hash(blockModel.getHash())
                    .timeStamp(blockModel.getTimeStamp())
                    .nonce(blockModel.getNonce())
                    .build();
            bloqueRepo.save(be);
            // Registrar ganancia al minero
            Ganancia g = Ganancia.builder()
                    .idmin(elegido.getId())
                    .idbloque(blockModel.getId())
                    .ganancia(Contants.MINER_REWARD)
                    .fecha(LocalDate.now())
                    .hora(LocalTime.now())
                    .build();
            gananciaRepo.save(g);
            // Actualizar cada transacción: estado, saldos y envío de correo
            for (Transaction t : trio) {
                // actualizar estado
                t.setEstado("PROCESADA");
                txRepo.save(t);
                // ajustar saldos
                Optional<UserDetail> optRem = userService.findById(t.getId1());
                Optional<UserDetail> optRec = userService.findById(t.getId2());
                if (optRem.isEmpty() || optRec.isEmpty()) {
                    throw new IllegalStateException("Usuario remitente o receptor no existe");
                }
                UserDetail rem = optRem.get();
                UserDetail rec = optRec.get();
                rem.setSaldo(rem.getSaldo() - t.getCantidad());
                rec.setSaldo(rec.getSaldo() + t.getCantidad());
                userService.save(rem);
                userService.save(rec);
                // enviar correo al remitente
                emailService.enviarCorreoConfirmacion(rem.getEmail(), t);
            }
            // Si quedan más pendientes, se procesarán en la siguiente invocación
        }
    }
}