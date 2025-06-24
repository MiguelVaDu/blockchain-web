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
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<UserDetail> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    public Optional<UserDetail> findById(Long id) {
        return userRepo.findById(id);
    }
    public UserDetail save(UserDetail user) {
        // Si es nuevo o se actualiza password: manejar cifrado
        if (user.getPassword() != null) {
            // Verificar si la contraseña ya parece codificada? Asumimos siempre nueva -> codificar
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepo.save(user);
    }
    public void updateSaldo(Long userId, double nuevoSaldo) {
        userRepo.findById(userId).ifPresent(u->{ u.setSaldo(nuevoSaldo); userRepo.save(u); });
    }
    public List<UserDetail> findAllExcept(Long id) {
        List<UserDetail> all = userRepo.findAll();
        return all.stream().filter(u->!u.getId().equals(id)).collect(Collectors.toList());
    }
}
