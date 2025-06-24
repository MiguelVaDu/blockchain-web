package com.tads.blockchainweb.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.*;

@Entity
@Table(name="bloques")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BloqueEntity {
    @Id
    private Integer id; // se asigna manualmente según el conteo de bloques

    @Column(columnDefinition="TEXT")
    private String transacciones; // concatenación o JSON de las transacciones incluidas

    private String hash;
    private String previousHash;
    private long timeStamp;
    private int nonce;
}
