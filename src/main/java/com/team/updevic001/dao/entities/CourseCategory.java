package com.team.updevic001.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course_category")
public class CourseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    @OneToOne
    private CourseCategory category;

    @OneToMany(mappedBy = "category")
    private List<Course> courses;
}
