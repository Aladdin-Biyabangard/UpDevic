package com.team.updevic001.controllers;

import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.model.dtos.request.TeacherDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "ele")
public class UserControllerImpl {

    @PostMapping(name = "nem")
    public void user(@RequestBody TeacherDto teacherDto) {

    }
}
