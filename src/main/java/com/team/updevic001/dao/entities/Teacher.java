package com.team.updevic001.dao.entities;

import com.team.updevic001.model.enums.Specialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teachers")
@Inheritance(strategy = InheritanceType.JOINED)
public class Teacher extends User {

    @Enumerated(EnumType.STRING)
    private Specialty speciality;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "social_link")
    private String socialLink;

    @Column(name = "hire_date")
    @CreationTimestamp
    private LocalDateTime hireDate;

//  Eger muellimin oldugu kurslari gormek kimi bir funksiya lazim olsa commentden cixarariq
//    @OneToMany(mappedBy = "teacher")
//    private List<CourseTeacher> courseTeachers;

}