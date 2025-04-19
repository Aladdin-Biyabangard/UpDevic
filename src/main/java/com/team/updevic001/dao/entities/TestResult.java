package com.team.updevic001.dao.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResult {

    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    private double score;

    @PrePersist
    public void generatedId() {
        if (this.id == null) {
            this.id = NanoIdUtils.randomNanoId().substring(0, 12);
        }
    }
}
