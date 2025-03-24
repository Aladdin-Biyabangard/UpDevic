package com.team.updevic001.dao.entities;

import com.team.updevic001.model.enums.CourseCategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course_category")
public class CourseCategory {

    @Id
    @Column(unique = true, nullable = false, length = 36)
    private String uuid;

    @Enumerated(EnumType.STRING)
    private CourseCategoryType category;

    @OneToMany(mappedBy = "category")
    private List<Course> courses;

    @PrePersist
    public void generateUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
