package com.team.updevic001.dao.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teacher_courses")
public class TeacherCourse {

    @Id
    @Column(unique = true, nullable = false, length = 12)    private String uuid;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @CreationTimestamp
    private LocalDateTime assignedAt;

    @PrePersist
    public void generateStudentNumber() {
        if (this.uuid == null) {
            this.uuid = NanoIdUtils.randomNanoId().substring(0,12);        }
    }

}
