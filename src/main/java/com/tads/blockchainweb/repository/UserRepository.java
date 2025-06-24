package com.tads.blockchainweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tads.blockchainweb.model.UserDetail;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByUsername(String username);
}
