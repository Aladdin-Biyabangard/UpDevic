package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.services.interfaces.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminServiceImpl;

    @PostMapping(path = "assign/teacher/{studentId}")
    public ResponseEntity<String> assignTeacherProfile(@PathVariable String studentId) {
        adminServiceImpl.assignTeacherProfile(studentId);
        return ResponseEntity.ok("Teacher profile created successfully") ;
    }


    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable String id) {
        adminServiceImpl.activateUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable String id) {
        adminServiceImpl.deactivateUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable String id,
                                                 @RequestParam Role role) {
        adminServiceImpl.assignRoleToUser(id, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseUserDto>> getAllUsers() {
        List<ResponseUserDto> allUsers = adminServiceImpl.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping(path = "/role")
    public ResponseEntity<List<ResponseUserDto>> getUserByRole(@RequestParam Role role) {
        List<ResponseUserDto> userByRole = adminServiceImpl.getUsersByRole(role);
        return new ResponseEntity<>(userByRole, HttpStatus.OK);
    }

    @GetMapping(path = "/count")
    public ResponseEntity<Long> getUsersCount() {
        return new ResponseEntity<>(adminServiceImpl.countUsers(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/all")
    public ResponseEntity<Void> deleteUsers() {
        adminServiceImpl.deleteUsers();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}/role")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable String id,
                                                   @RequestParam Role role) {
        adminServiceImpl.removeRoleFromUser(id, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
