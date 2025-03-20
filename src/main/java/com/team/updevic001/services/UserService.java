package com.team.updevic001.services;

import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.model.dtos.request.TeacherDto;
import com.team.updevic001.model.dtos.request.UserDto;
import com.team.updevic001.model.dtos.request.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;

import java.util.List;

public interface UserService {

    ResponseUserDto newUser(TeacherDto teacherDto);

    void updateUserProfileInfo(String uuid, UserProfileDto userProfileDto);

    void updateUserPassword(String uuid, String oldPassword, String newPassword);

    void activateUser(String uuid);

    void deactivateUser(String uuid);

    void addRole(String uuid, UserRole role);

    ResponseUserDto getUserById(String uuid);

    List<ResponseUserDto> getUser(String query);

    List<ResponseUserDto> getUserByRole(Role role);

    List<ResponseUserDto> getAllUsers();

    Long countUsers();

    void sendPasswordResetEmail(String uuid);

    void deleteUser(String uuid);

    void deleteUsers();


}
