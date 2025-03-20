package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    Optional<UserRole> findByName(Role role);
}
