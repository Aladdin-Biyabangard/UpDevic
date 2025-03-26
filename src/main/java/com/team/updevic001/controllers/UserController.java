package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.request.ChangePasswordDto;
import com.team.updevic001.model.dtos.request.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.services.AdminService;
import com.team.updevic001.services.UserService;
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
    // BU METODALRDA PRINCIPAL dan istifade edeciyik. Security qosulandan sonra


    @PutMapping("/{uuid}/profile")
    public ResponseEntity<Void> updateUserProfileInfo(@PathVariable String uuid,
                                                      @RequestBody UserProfileDto userProfileDto) {
        userService.updateUserProfileInfo(uuid, userProfileDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{uuid}/password")
    public ResponseEntity<Void> updateUserPassword(@PathVariable String uuid, @Valid @RequestBody ChangePasswordDto passwordDto) {
        userService.updateUserPassword(uuid, passwordDto);
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


    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uuid) {
        userService.deleteUser(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
