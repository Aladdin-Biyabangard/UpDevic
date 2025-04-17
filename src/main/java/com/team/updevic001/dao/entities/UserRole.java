    package com.team.updevic001.dao.entities;

    import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
    import com.team.updevic001.model.enums.Role;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Entity
    @Table(name = "roles")
    public class UserRole {

        @Id
        @Column(unique = true, nullable = false, length = 12)
        private String id;

        @Enumerated(value = EnumType.STRING)
        @Column(unique = true, nullable = false)
        private Role name;

        @PrePersist
        public void generatedId() {
            if (this.id == null) {
                this.id = NanoIdUtils.randomNanoId().substring(0, 12);
            }
        }
    }
