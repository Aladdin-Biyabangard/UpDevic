package com.team.updevic001.dao.entities;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @Column(unique = true, nullable = false, length = 12)
    private String uuid;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "bio", length = 500)
    private String bio;

    @ElementCollection
    @CollectionTable(name = "user_social_links", joinColumns = @JoinColumn(name = "user_profile_id"))
    @Column(name = "social_link")
    private List<String> socialLinks;

    @ElementCollection
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_profile_id"))
    @Column(name = "skill")
    private List<String> skills;


    @PrePersist
    public void generateStudentNumber() {
        if (this.uuid == null) {
            this.uuid = NanoIdUtils.randomNanoId().substring(0, 12);
        }
    }
}
