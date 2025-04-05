package com.team.updevic001.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Data
@Table(name = "students")
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends User {

//    @Column(name = "student_number", nullable = false)
//    private String studentNumber;

    @Column(name = "enrolled_date")
    @CreationTimestamp
    private LocalDateTime enrolledDate;

    @OneToMany(mappedBy = "student")
    private List<TestResult> testResults = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "certificate_id", referencedColumnName = "id")
    private Certificate certificate;

}