package com.team.updevic001.controllers;

import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.model.dtos.request.user.UserDto;
import com.team.updevic001.model.dtos.request.user.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserController {

    ResponseEntity<ResponseUserDto> newUser(UserDto userDto);

    ResponseEntity<Void> updateUserProfileInfo(UUID uuid, UserProfileDto userProfileDto);

    ResponseEntity<Void> updateUserPassword(UUID uuid, String oldPassword, String newPassword);

    ResponseEntity<Void> activateUser(UUID uuid);

    ResponseEntity<Void> deactivateUser(UUID uuid);

    ResponseEntity<Void> addRole(UUID uuid, UserRole role);

    ResponseEntity<ResponseUserDto> getUserById(UUID uuid);

    ResponseEntity<List<ResponseUserDto>> searchUsers(String query);

    ResponseEntity<List<ResponseUserDto>> getUserByRole(Role role);

    ResponseEntity<List<ResponseUserDto>> getAllUsers();


    ResponseEntity<Void> sendPasswordResetEmail(UUID uuid);

    ResponseEntity<Void> deleteUser(UUID uuid);

    ResponseEntity<Void> deleteUsers();
}
