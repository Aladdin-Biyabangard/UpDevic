package com.team.updevic001.dao.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@SuperBuilder
@Table(name = "students")
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends User {

//    @Column(name = "student_number", nullable = false)
//    private String studentNumber;

    @Column(name = "enrolled_date")
    @CreationTimestamp
    private LocalDateTime enrolledDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "certificate_id", referencedColumnName = "uuid")
    private Certificate certificate;

}