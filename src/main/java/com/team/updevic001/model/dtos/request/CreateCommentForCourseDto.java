package com.team.updevic001.model.dtos.request;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.dao.entities.User;

public class CreateCommentForCourseDto {

    private String content;

    private User user;

    private Course course;

}

