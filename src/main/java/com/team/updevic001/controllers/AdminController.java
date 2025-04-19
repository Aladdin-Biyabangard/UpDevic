package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.services.interfaces.AdminService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Adding a teacher profile to a student")
    @PostMapping(path = "assign/teacher/{email}")
    public ResponseEntity<String> assignTeacherProfile(@PathVariable String email) {
        adminServiceImpl.assignTeacherProfile(email);
        return ResponseEntity.ok("Teacher profile created successfully");
    }

    @Operation(summary = "Activates the user")
    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateUser(@PathVariable String id) {
        adminServiceImpl.activateUser(id);
        return ResponseEntity.ok("User activated.");
    }

    @Operation(summary = "Deactivates the user")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable String id) {
        adminServiceImpl.deactivateUser(id);
        return ResponseEntity.ok("User deactivated.");
    }

    @Operation(summary = "Adds a role to a user")
    @PutMapping("/{id}/role")
    public ResponseEntity<String> assignRoleToUser(@PathVariable String id,
                                                   @RequestParam Role role) {
        adminServiceImpl.assignRoleToUser(id, role);
        return ResponseEntity.ok("User has been assigned a role.");
    }

    @Operation(summary = "Shows all users")
    @GetMapping("/all")
    public ResponseEntity<List<ResponseUserDto>> getAllUsers() {
        List<ResponseUserDto> allUsers = adminServiceImpl.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @Operation(summary = "Shows users matching the role")
    @GetMapping(path = "/role")
    public ResponseEntity<List<ResponseUserDto>> getUserByRole(@RequestParam Role role) {
        List<ResponseUserDto> userByRole = adminServiceImpl.getUsersByRole(role);
        return new ResponseEntity<>(userByRole, HttpStatus.OK);
    }

    @Operation(summary = "Shows the number of users")
    @GetMapping(path = "/count")
    public ResponseEntity<Long> getUsersCount() {
        return new ResponseEntity<>(adminServiceImpl.countUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Removes a user's role!")
    @DeleteMapping(path = "/{id}/role")
    public ResponseEntity<String> removeRoleFromUser(@PathVariable String id,
                                                     @RequestParam Role role) {
        adminServiceImpl.removeRoleFromUser(id, role);
        return ResponseEntity.ok("Role removed!");
    }

    @DeleteMapping(path = "")
    public ResponseEntity<String> permanentlyDeleteUser(@RequestParam String userId) {
        adminServiceImpl.permanentlyDeleteUser(userId);
        return ResponseEntity.ok("User deleted!");
    }

    @Operation(summary = "All User Deletes.")
    @DeleteMapping(path = "/all")
    public ResponseEntity<String> deleteUsers() {
        adminServiceImpl.deleteUsers();
        return ResponseEntity.ok("Users deleted!");
    }

}
