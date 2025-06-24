package com.tads.blockchainweb;

import com.tads.blockchainweb.model.Miner;
import com.tads.blockchainweb.model.UserDetail;
import com.tads.blockchainweb.repository.MinerRepository;
import com.tads.blockchainweb.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlockchainWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlockchainWebApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(UserService userService, MinerRepository minerRepo) {
        return args -> {
            if(userService.findByUsername("user1").isEmpty()) {
                // Crear 6 usuarios con rol USER; contraseña "password"
                for(int i=1;i<=6;i++){
                    UserDetail u = UserDetail.builder()
                            .username("user"+i)
                            .password("password")
                            .rol("ROLE_USER")
                            .nomcompleto("User "+i)
                            .dni("DNI"+i)
                            .saldo(1000.0)
                            .firmadigital("") // opcional
                            .email("user"+i+"@example.com")
                            .build();
                    userService.save(u);
                }
            }
            if(minerRepo.count()==0){
                for(int i=1;i<=3;i++){
                    Miner m = Miner.builder()
                            .nombre("Miner"+i)
                            .dni("MDNI"+i)
                            .clave("clave"+i)
                            .build();
                    minerRepo.save(m);
                }
            }
            // Opcional: crear bloque génesis en BD si no existe
            // ...
        };
    }
}

