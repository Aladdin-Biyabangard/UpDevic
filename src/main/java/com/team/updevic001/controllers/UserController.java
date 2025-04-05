package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.UserProfileDto;
import com.team.updevic001.model.dtos.request.security.ChangePasswordDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.services.interfaces.UserService;
import jakarta.validation.Valid;
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

    @PutMapping("/profile")
    public ResponseEntity<Void> updateUserProfileInfo(@RequestBody UserProfileDto userProfileDto) {
        userService.updateUserProfileInfo(userProfileDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updateUserPassword(@Valid @RequestBody ChangePasswordDto passwordDto) {
        userService.updateUserPassword(passwordDto);
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

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
