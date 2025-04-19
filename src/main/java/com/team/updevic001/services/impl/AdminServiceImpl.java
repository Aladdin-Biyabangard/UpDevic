package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.UserMapper;
import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.dao.repositories.TeacherRepository;
import com.team.updevic001.dao.repositories.UserRepository;
import com.team.updevic001.dao.repositories.UserRoleRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.interfaces.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;
    private final TeacherRepository teacherRepository;
    private final UserServiceImpl userServiceImpl;

    @Override
    public void assignTeacherProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Enter the email you registered with."));
        UserRole userRole = userRoleRepository.findByName(Role.TEACHER).orElseGet(() -> {
            UserRole role = UserRole.builder()
                    .name(Role.TEACHER)
                    .build();
            return userRoleRepository.save(role);

        });
        if (!user.getRoles().contains(userRole)) {
            user.getRoles().add(userRole);
            Teacher teacher = new Teacher();
            teacher.setUser(user);
            userRepository.save(user);
            teacherRepository.save(teacher);
        }
    }


    @Override
    public List<ResponseUserDto> getAllUsers() {
        log.info("Finding all users!");
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            log.info("No user found!");
        } else {
            log.info("Found {} users.", users.size());
        }

        return userMapper.toResponseList(users, ResponseUserDto.class);
    }

    @Override
    public void activateUser(String id) {
        log.info("Activating user with ID: {}", id);
        User user = userServiceImpl.findUserById(id);
        user.setStatus(Status.ACTIVE);
        saveUser(user);
        log.info("User with ID:{} status activated!", id);
    }

    @Override
    public void deactivateUser(String id) {
        log.info("Deactivating user with ID: {}", id);
        User user = userServiceImpl.findUserById(id);
        user.setStatus(Status.INACTIVE);
        saveUser(user);
        log.info("User with ID:{} status deactivated!", id);
    }

    @Override
    public void assignRoleToUser(String id, Role role) {
        log.info("Assigning role {} to user with ID: {}", role, id);
        User user = userServiceImpl.findUserById(id);
        UserRole userRole = userRoleRepository.findByName(role).orElseGet(() -> {
            UserRole newRole = UserRole.builder()
                    .name(role)
                    .build();
            return userRoleRepository.save(newRole);
        });
        saveUserRole(userRole);
        user.getRoles().add(userRole);
        saveUser(user);
        log.info("Role {} successfully added to user with ID: {}", role, id);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(String userId, Role role) {
        log.info("Removing role {} from user with ID: {}", role, userId);
        User user = userServiceImpl.findUserById(userId);

        UserRole findRole = user.getRoles()
                .stream()
                .filter(userRole -> Objects.equals(userRole.getName(), role))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("This user does not have such a role."));

        user.getRoles().remove(findRole);
        userRoleRepository.delete(findRole);

        log.info("Role {} successfully removed from user with ID: {}", role, userId);
    }

    @Override
    public List<ResponseUserDto> getUsersByRole(Role role) {
        log.info("Fetching users with role: {}", role);
        List<User> users = userRepository.findUsersByRole(role);
        if (users.isEmpty()) {
            log.info("No users found with role: {}", role);
            throw new ResourceNotFoundException("User not found");
        } else {
            log.info("Found {} users with role: {}", users.size(), role);
        }

        return userMapper.toResponseList(users, ResponseUserDto.class);
    }


    @Override
    public void deleteUsers() {
        log.info("Deleting all users!");
        userRepository.deleteAll();
        log.info("All users successfully deleted!");
        userRepository.resetAutoIncrement();
        log.info("User Id successfully reset!");
    }

    @Override
    public Long countUsers() {
        log.info("Counting all users.");
        Long userCount = userRepository.count();
        log.info("Total number of users: {}", userCount);
        return userCount;
    }

    private void saveUser(User user) {
        log.info("Saving user with ID: {}", user.getId());
        userRepository.save(user);
    }

    private void saveUserRole(UserRole userRole) {
        log.info("Saving user role: {}", userRole.getName());
        userRoleRepository.save(userRole);
    }

    @Override
    public void permanentlyDeleteUser(String userId) {
        log.info("Attempting to delete user with ID: {}", userId);
        userRepository.deleteById(userId);
        log.info("User successfully deleted.");
    }
}
