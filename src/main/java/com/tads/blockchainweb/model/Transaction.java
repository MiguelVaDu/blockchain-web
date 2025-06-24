package com.tads.blockchainweb.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name="transacciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalTime hora;

    private Long id1; // remitente (user.id)
    private Long id2; // receptor

    private double cantidad;

    private String estado; // "PENDIENTE", "PROCESADA"
}

