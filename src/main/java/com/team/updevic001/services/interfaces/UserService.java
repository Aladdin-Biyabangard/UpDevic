package com.team.updevic001.services.interfaces;

import com.team.updevic001.model.dtos.request.StudentDto;
import com.team.updevic001.model.dtos.request.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;

import java.util.List;

public interface UserService {

    ResponseUserDto newUser(StudentDto user);

    ResponseUserDto getUserById(String id);

    List<ResponseUserDto> getUser(String query);

    void updateUserProfileInfo(String id, UserProfileDto userProfileDto);

    void updateUserPassword(String id, String oldPassword, String newPassword);

//    void sendPasswordResetEmail(String id);

    void deleteUser(String id);


}
