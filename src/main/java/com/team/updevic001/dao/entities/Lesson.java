package com.team.updevic001.dao.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "lessons")
public class Lesson {

    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String uuid;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private Course course;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments;

    @PrePersist
    public void generateStudentNumber() {
        if (this.uuid == null) {
            this.uuid = NanoIdUtils.randomNanoId().substring(0, 12);
        }
    }

}