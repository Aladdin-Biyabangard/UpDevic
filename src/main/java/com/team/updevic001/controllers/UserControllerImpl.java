package com.team.updevic001.controllers;

import com.team.updevic001.dao.entities.TestClass;
import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.dao.repositories.TestRepo;
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

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserControllerImpl {

    private final UserService userService;
    private final TestRepo testRepo;

    @PostMapping(path = "test-/")
    public void testCl(@RequestBody TestClass testClass) {
        testRepo.save(testClass);
    }


    @PostMapping
    public ResponseEntity<ResponseUserDto> newUser(@RequestBody UserDto userDto) {
        ResponseUserDto responseUserDto = userService.newUser(userDto);
        return new ResponseEntity<>(responseUserDto, HttpStatus.CREATED);

    }


    @PutMapping("/{uuid}/profile")
    public ResponseEntity<Void> updateUserProfileInfo(@PathVariable String uuid,
                                                      @RequestBody UserProfileDto userProfileDto) {
        userService.updateUserProfileInfo(uuid, userProfileDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}/password")
    public ResponseEntity<Void> updateUserPassword(@PathVariable String uuid,
                                                   @RequestParam String oldPassword,
                                                   @RequestParam String newPassword) {
        userService.updateUserPassword(uuid, oldPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable String uuid) {
        userService.activateUser(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable String uuid) {
        userService.deactivateUser(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}/role")
    public ResponseEntity<Void> addRole(@PathVariable String uuid,
                                        @RequestBody UserRole role) {
        userService.addRole(uuid, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseUserDto> getUserById(@PathVariable String uuid) {
        ResponseUserDto userById = userService.getUserById(uuid);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResponseUserDto>> searchUsers(@RequestParam String query) {
        List<ResponseUserDto> user = userService.getUser(query);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/role")
    public ResponseEntity<List<ResponseUserDto>> getUserByRole(@RequestParam Role role) {
        List<ResponseUserDto> userByRole = userService.getUserByRole(role);
        return new ResponseEntity<>(userByRole, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseUserDto>> getAllUsers() {
        List<ResponseUserDto> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping(path = "/{uuid}/reset")
    public ResponseEntity<Void> sendPasswordResetEmail(@PathVariable String uuid) {
        userService.sendPasswordResetEmail(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uuid) {
        userService.deleteUser(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/all")
    public ResponseEntity<Void> deleteUsers() {
        userService.deleteUsers();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
