package com.team.updevic001.services;

import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.model.dtos.request.user.UserDto;
import com.team.updevic001.model.dtos.request.user.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;

import java.util.List;
import java.util.UUID;

public interface UserService {

    ResponseUserDto newUser(UserDto userDto);

    void updateUserProfileInfo(UUID uuid, UserProfileDto userProfileDto);

    void updateUserPassword(UUID uuid, String oldPassword, String newPassword);

    void activateUser(UUID uuid);

    void deactivateUser(UUID uuid);

    void addRole(UUID uuid, UserRole role);

    ResponseUserDto getUserById(UUID uuid);

    List<ResponseUserDto> getUser(String query);

    List<ResponseUserDto> getUserByRole(Role role);

    List<ResponseUserDto> getAllUsers();

    Long countUsers();

    void sendPasswordResetEmail(UUID uuid);

    void deleteUser(UUID uuid);

    void deleteUsers();


}
