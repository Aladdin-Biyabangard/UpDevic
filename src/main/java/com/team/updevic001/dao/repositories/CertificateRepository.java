package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
}
