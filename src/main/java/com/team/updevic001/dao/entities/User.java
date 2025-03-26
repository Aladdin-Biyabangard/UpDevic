    package com.team.updevic001.dao.entities;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.team.updevic001.model.enums.Status;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.hibernate.annotations.CreationTimestamp;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.List;
    import java.util.UUID;
    import java.util.stream.Collectors;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Builder
    @Table(name = "users")
    @Inheritance(strategy = InheritanceType.JOINED)
    public class User implements UserDetails {

        @Id
        @Column(unique = true, nullable = false, length = 36)
        private String uuid;

        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Column(name = "last_name")
        private String lastName;

        @Column(name = "password", nullable = false)
        private String password;

        @Column(name = "email", unique = true)
        private String email;

        @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
        @Enumerated(EnumType.STRING)
        private Status status = Status.PENDING;

        @Column(name = "created_at")
        @CreationTimestamp
        private LocalDateTime createdAt;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<Comment> comments;

        @ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        private List<UserRole> roles = new ArrayList<>();

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private UserProfile userProfile;


        @PrePersist
        public void generateUuid() {
            if (this.uuid == null) {
                this.uuid = UUID.randomUUID().toString();
            }
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return roles.stream()
                    .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getName()))
                    .collect(Collectors.toList());
        }

        @Override
        public String getPassword() {
            return this.password;
        }

        @Override
        public String getUsername() {
            return this.email;
        }
    }