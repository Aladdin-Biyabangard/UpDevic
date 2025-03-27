package com.team.updevic001.services;

import com.team.updevic001.model.dtos.request.ChangePasswordDto;
import com.team.updevic001.model.dtos.request.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;

import java.util.List;

public interface UserService {


    ResponseUserDto getUserById(String uuid);

    List<ResponseUserDto> getUser(String query);

    void updateUserProfileInfo(UserProfileDto userProfileDto);

    void updateUserPassword(ChangePasswordDto passwordDto);

    void deleteUser();


}
