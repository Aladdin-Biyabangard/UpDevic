package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByIdAndExpiresAtAfter(String id, LocalDateTime now);
}