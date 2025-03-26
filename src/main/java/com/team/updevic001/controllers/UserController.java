package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.services.AdminService;
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
public class UserController {

    private final UserService userService;

//    @PostMapping
//    public ResponseEntity<ResponseUserDto> newUser(@RequestBody StudentDto user) {
//        ResponseUserDto responseUserDto = userService.newUser(user);
//        return new ResponseEntity<>(responseUserDto, HttpStatus.CREATED);
//
//    }

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


//    @GetMapping(path = "/{uuid}/reset")
//    public ResponseEntity<Void> sendPasswordResetEmail(@PathVariable String uuid) {
//        userService.sendPasswordResetEmail(uuid);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uuid) {
        userService.deleteUser(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
