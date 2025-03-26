package com.team.updevic001.dao.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.team.updevic001.model.enums.CourseCategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course_category")
public class CourseCategory {

    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String uuid;

    @Enumerated(EnumType.STRING)
    private CourseCategoryType category;

    @OneToMany(mappedBy = "category")
    private List<Course> courses;

    @PrePersist
    public void generateStudentNumber() {
        if (this.uuid == null) {
            this.uuid = NanoIdUtils.randomNanoId().substring(0, 12);
        }
    }
}
