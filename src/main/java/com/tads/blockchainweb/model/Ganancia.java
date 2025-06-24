package com.tads.blockchainweb.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ganancia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idmin;   // id de Miner
    private int idbloque; // identificador del bloque minado
    private double ganancia;
    private LocalDate fecha;
    private LocalTime hora;
}
