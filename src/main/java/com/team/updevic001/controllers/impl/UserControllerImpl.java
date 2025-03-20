package com.team.updevic001.controllers.impl;

import com.team.updevic001.controllers.UserController;
import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.model.dtos.request.user.UserDto;
import com.team.updevic001.model.dtos.request.user.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @PostMapping
    @Override
    public ResponseEntity<ResponseUserDto> newUser(@RequestBody UserDto userDto) {
        ResponseUserDto responseUserDto = userService.newUser(userDto);
        return new ResponseEntity<>(responseUserDto, HttpStatus.CREATED);

    }


    @PutMapping("/{uuid}/profile")
    @Override
    public ResponseEntity<Void> updateUserProfileInfo(@PathVariable UUID uuid,
                                                      @RequestBody UserProfileDto userProfileDto) {
        userService.updateUserProfileInfo(uuid, userProfileDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}/password")
    @Override
    public ResponseEntity<Void> updateUserPassword(@PathVariable UUID uuid,
                                                   @RequestParam String oldPassword,
                                                   @RequestParam String newPassword) {
        userService.updateUserPassword(uuid, oldPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}/activate")
    @Override
    public ResponseEntity<Void> activateUser(@PathVariable UUID uuid) {
        userService.activateUser(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}/deactivate")
    @Override
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID uuid) {
        userService.deactivateUser(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}/role")
    @Override
    public ResponseEntity<Void> addRole(@PathVariable UUID uuid,
                                        @RequestParam UserRole role) {
        userService.addRole(uuid, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<ResponseUserDto> getUserById(@PathVariable UUID uuid) {
        ResponseUserDto userById = userService.getUserById(uuid);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Override
    public ResponseEntity<List<ResponseUserDto>> searchUsers(@RequestParam String query) {
        List<ResponseUserDto> user = userService.getUser(query);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ResponseUserDto>> getUserByRole(Role role) {
        List<ResponseUserDto> userByRole = userService.getUserByRole(role);
        return new ResponseEntity<>(userByRole, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ResponseUserDto>> getAllUsers() {
        List<ResponseUserDto> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> sendPasswordResetEmail(UUID uuid) {
        userService.sendPasswordResetEmail(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID uuid) {
        userService.deleteUser(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUsers() {
        userService.deleteUsers();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
