package com.leoosato.project.TokioMarine.repository;


import com.leoosato.project.TokioMarine.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<UserModel> findByUsername(String username);
    boolean existsByAccountNumber(String accountNumber);
    Optional<UserModel> findByAccountNumber(String accountNumber);

}
