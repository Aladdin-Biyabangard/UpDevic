//package com.team.updevic001.dao.entities;
//
//import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
//import com.team.updevic001.model.enums.CourseCategoryType;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "course_category")
//public class CourseCategory {
//
//    @Id
//    @Column(unique = true, nullable = false, length = 12)
//    private String id;
//
//    @Enumerated(EnumType.STRING)
//    private CourseCategoryType category;
//
//    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private List<Course> courses = new ArrayList<>();
//
//    @PrePersist
//    public void generatedId() {
//        if (this.id == null) {
//            this.id = NanoIdUtils.randomNanoId().substring(0, 12);
//        }
//    }
//}
