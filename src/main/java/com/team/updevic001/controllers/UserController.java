package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.services.interfaces.UserService;
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

    @PutMapping("/{id}/profile")
    public ResponseEntity<Void> updateUserProfileInfo(@PathVariable String id,
                                                      @RequestBody UserProfileDto userProfileDto) {
        userService.updateUserProfileInfo(id, userProfileDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updateUserPassword(@PathVariable String id,
                                                   @RequestParam String oldPassword,
                                                   @RequestParam String newPassword) {
        userService.updateUserPassword(id, oldPassword, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getUserById(@PathVariable String id) {
        ResponseUserDto userById = userService.getUserById(id);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResponseUserDto>> searchUsers(@RequestParam String query) {
        List<ResponseUserDto> user = userService.getUser(query);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


//    @GetMapping(path = "/{id}/reset")
//    public ResponseEntity<Void> sendPasswordResetEmail(@PathVariable String id) {
//        userService.sendPasswordResetEmail(id);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
