package com.poolfy.api.entity;

import com.poolfy.api.entity.enums.ExperienceLevel;
import com.poolfy.api.entity.enums.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_user_type")
    private UserType preferredUserType;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_experience_level")
    private ExperienceLevel preferredExperienceLevel;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
