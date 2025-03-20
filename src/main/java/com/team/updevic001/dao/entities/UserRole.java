package com.team.updevic001.dao.entities;

import com.team.updevic001.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "roles")
public class UserRole {

    @Id
    @Column(unique = true, nullable = false, length = 36)
    private String uuid;

    @Enumerated(EnumType.STRING)
    private Role name;

    @PrePersist
    public void generateUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString().substring(0, 35);
        }
    }
}
