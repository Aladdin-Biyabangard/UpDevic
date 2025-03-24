package com.team.updevic001.dao.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "otp")
public class Otp {
    @Id
    @Column(unique = true, nullable = false, length = 36)
    String uuid;
    Integer code;
    String email;
    LocalDateTime expirationTime;

    @PrePersist
    public void generateUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString().substring(0, 35);
        }
    }
}
