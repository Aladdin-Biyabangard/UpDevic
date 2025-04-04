package com.team.updevic001.dao.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String token;
    private LocalDateTime expirationTime;

}