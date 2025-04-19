package com.team.updevic001.dao.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String id;

    private String taskNumbers;
    private String questions;

    @ElementCollection
    private List<String> options = new ArrayList<>();

    private String correctAnswer;


    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    @PrePersist
    public void generatedId() {
        if (this.id == null) {
            this.id = NanoIdUtils.randomNanoId().substring(0, 12);
        }
        if (this.taskNumbers == null) {
            this.taskNumbers = UUID.randomUUID().toString().substring(0, 5);
        }
    }
}
