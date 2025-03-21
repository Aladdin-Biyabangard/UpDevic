package com.team.updevic001.services.impl;

import com.team.updevic001.config.mappers.UserMapper;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.dao.repositories.UserRepository;
import com.team.updevic001.dao.repositories.UserRoleRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.AdminService;
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
    private final UserServiceImpl userServiceImpl;

    @Override
    public List<ResponseUserDto> getAllUsers() {
        log.info("Finding all users!");
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            log.info("Not user found!");
        }

        return userMapper.toResponseList(users, ResponseUserDto.class);
    }

    @Override
    public void activateUser(String uuid) {
        User user = findUserById(uuid);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        log.info("User with ID:{} status activated!", uuid);
    }

    @Override
    public void deactivateUser(String uuid) {
        User user = findUserById(uuid);
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
        log.info("User with ID:{} status deactivated!", uuid);
    }

    @Override
    public void assignRoleToUser(String uuid, Role role) {
        User user = findUserById(uuid);
        UserRole userRole = UserRole.builder()
                .name(role).build();
        userRoleRepository.save(userRole);
        user.getRoles().add(userRole);
        userRepository.save(user);
        log.info("New rol successfully added!");
    }

    @Override
    @Transactional
    public void removeRoleFromUser(String userId, Role role) {
        User user = userServiceImpl.findUserById(userId);

        UserRole findRole = user.getRoles()
                .stream()
                .filter(userRole -> Objects.equals(userRole.getName(), role))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("This user does not have such a role."));

        user.getRoles().remove(findRole);
        userRoleRepository.delete(findRole);

        log.info("User role : {} successfully deleted!", role);
    }

    @Override
    public List<ResponseUserDto> getUsersByRole(Role role) {

        List<User> users = userRepository.findUsersByRole(role);
        if (!users.isEmpty()) {
            log.info("There are no users matching this ROLE: {}.", role);
            return userMapper.toResponseList(users, ResponseUserDto.class);
        }
        log.info("There is no user with this ROLE: {}", role);
        throw new ResourceNotFoundException("User not found");
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
        return userRepository.count();
    }

    private User findUserById(String uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> {
            log.error("User not found with ID: {}", uuid);
            return new ResourceNotFoundException("User not found");
        });
    }
}
