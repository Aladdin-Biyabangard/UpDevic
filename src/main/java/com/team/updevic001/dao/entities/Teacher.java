package com.team.updevic001.dao.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.team.updevic001.model.enums.Specialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String uuid;

    @Enumerated(EnumType.STRING)
    private Specialty speciality;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "hire_date")
    @CreationTimestamp
    private LocalDateTime hireDate;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TeacherCourse> teacherCourses;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "uuid")
    private User user;

    @PrePersist
    public void generateStudentNumber() {
        if (this.uuid == null) {
            this.uuid = NanoIdUtils.randomNanoId().substring(0, 12);
        }
    }
}