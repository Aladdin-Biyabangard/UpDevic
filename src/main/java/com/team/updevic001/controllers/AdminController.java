package com.team.updevic001.controllers;

import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/{uuid}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable String uuid) {
        adminService.activateUser(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable String uuid) {
        adminService.deactivateUser(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}/role")
    public ResponseEntity<Void> assignRoleToUser(@PathVariable String uuid,
                                                 @RequestParam Role role) {
        adminService.assignRoleToUser(uuid, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseUserDto>> getAllUsers() {
        List<ResponseUserDto> allUsers = adminService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping(path = "/role")
    public ResponseEntity<List<ResponseUserDto>> getUserByRole(@RequestParam Role role) {
        List<ResponseUserDto> userByRole = adminService.getUsersByRole(role);
        return new ResponseEntity<>(userByRole, HttpStatus.OK);
    }

    @GetMapping(path = "/count")
    public ResponseEntity<Long> getUsersCount() {
        return new ResponseEntity<>(adminService.countUsers(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/all")
    public ResponseEntity<Void> deleteUsers() {
        adminService.deleteUsers();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{uuid}/role")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable String uuid,
                                                   @RequestParam Role role) {
        adminService.removeRoleFromUser(uuid, role);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
