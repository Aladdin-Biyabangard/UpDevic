package com.team.updevic001.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "certificates")
public class Certificate {

    @Id
    @Column(unique = true, nullable = false, length = 36)
    private String uuid;

    @Column(name = "certificate_content")
    private String certificateContent;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "issue_date")
    @CreationTimestamp
    private LocalDateTime issueDate;

    @PrePersist
    public void generateUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
