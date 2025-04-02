package com.team.updevic001.configuration.enums;

import com.team.updevic001.model.enums.Role;
import lombok.Getter;

@Getter
public enum AuthMapping {

    TEACHER_ADMIN(new String[]{Role.TEACHER.name(), Role.ADMIN.name()}, new String[]{
            "/api/course/{courseId}",
            "/api/course/{courseId}/lessons/{lessonId}",
            "/api/teacher/{teacherId}"
    }),
    //    TEACHER(new String[]{Role.TEACHER.name(), Role.ADMIN.name()}, new String[]{
//            "/api/teacher/{teacherId}/course/assign",
//            "/api/teacher/{teacherId}/course/{courseId}/lesson/assign",
//            "/{teacherId}/course/{courseId}/update",
//            "/api/teacher/delete/{teacherId}"
//    }),
//    AUTHENTICATED(new String[]{}, new String[]{
//            "/api/course/comment/{courseId}"
//    }),

    ADMIN(new String[]{Role.ADMIN.name()}, new String[]{
            "/api/admin/**",
            "/api/teacher/delete/all"
    }),
    PERMIT_ALL(null, new String[]{
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/auth/**",
            "/api/course/search",
            "/api/course/{courseId}",
            "/api/course/all",
            "/api/course/category",
            "/api/course/{courseId}/lessons",
            "/api/course/{courseId}/lessons/{lessonId}",
            "/api/teacher/{teacherId}/courses",
            "/error"
    });

    private final String[] role;
    private final String[] urls;

    AuthMapping(String[] role, String[] urls) {
        this.role = role;
        this.urls = urls;
    }

}

