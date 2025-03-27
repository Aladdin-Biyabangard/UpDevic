package com.team.updevic001.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<com.team.updevic001.dao.entities.PasswordResetToken, Long> {
    Optional<com.team.updevic001.dao.entities.PasswordResetToken> findByToken(String token);
}
