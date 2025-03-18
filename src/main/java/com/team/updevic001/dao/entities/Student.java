package com.team.updevic001.dao.entities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
                                                        /**  Hazirdir.
                                                         * */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "students")
@Inheritance(strategy = InheritanceType.JOINED)
public class Student extends User {

    @Column(name = "studentNumber", nullable = false)
    private String studentNumber;


    @Column(name = "enrolled_date")
    @CreationTimestamp
    private LocalDateTime enrolledDate;


}