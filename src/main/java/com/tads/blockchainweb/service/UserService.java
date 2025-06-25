package com.tads.blockchainweb.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tads.blockchainweb.model.UserDetail;
import com.tads.blockchainweb.repository.UserRepository;

@Service
public class UserService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    public Optional<UserDetail> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    public Optional<UserDetail> findById(Long id) {
        return userRepo.findById(id);
    }
    public List<UserDetail> findAll() {
        return userRepo.findAll();
    }

    public UserDetail createUser(UserDetail user) {
        // solo para creación: codifica siempre
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public UserDetail updateSaldoOnly(Long userId, double nuevoSaldo) {
        return userRepo.findById(userId).map(u -> {
            u.setSaldo(nuevoSaldo);
            // NO tocar u.getPassword()
            return userRepo.save(u);
        }).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));
    }
    // si se necesita metodo save general, no recodificar si ya está codificado:
    public UserDetail save(UserDetail user) {
        String pass = user.getPassword();
        if (pass != null && !pass.startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(pass));
        }
        return userRepo.save(user);
    }
    public void changePassword(Long userId, String newPassword) {
        userRepo.findById(userId).ifPresent(u -> {
            u.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(u);
        });
    }

    public List<UserDetail> findAllExcept(Long id) {
        List<UserDetail> all = userRepo.findAll();
        return all.stream().filter(u->!u.getId().equals(id)).collect(Collectors.toList());
    }
}
