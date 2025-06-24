package com.tads.blockchainweb.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="userdetail")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String rol; // e.g., "ROLE_USER" o "ROLE_ADMIN"

    private String nomcompleto;
    private String dni;
    private double saldo;
    private String firmadigital;
    private String email;
}
