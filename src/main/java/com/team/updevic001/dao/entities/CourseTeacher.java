package com.team.updevic001.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course_teacher")
public class CourseTeacher {

    @Id
    @Column(unique = true, nullable = false, length = 36)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @PrePersist
    public void generateUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString().substring(0, 35);
        }
    }
}
