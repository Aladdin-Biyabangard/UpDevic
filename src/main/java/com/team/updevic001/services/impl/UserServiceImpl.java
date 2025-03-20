package com.team.updevic001.services.impl;

import com.team.updevic001.config.mappers.UserMapper;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.entities.UserProfile;
import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.dao.repositories.UserProfileRepository;
import com.team.updevic001.dao.repositories.UserRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.user.UserDto;
import com.team.updevic001.model.dtos.request.user.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserProfileRepository userProfileRepository;


    public ResponseUserDto newUser(UserDto userDto) {
        log.info("Creating a new user with email: {}", userDto.getEmail());

        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.warn("Email {} already exists!", userDto.getEmail());
            throw new IllegalArgumentException("Email already exists!");
        }


        User user = userMapper.toEntity(userDto, User.class);
        userRepository.save(user);
        log.info("User created successfully with ID: {}", user.getUuid());
        return userMapper.toResponse(user, ResponseUserDto.class);
    }

    public void updateUserProfileInfo(UUID uuid, UserProfileDto userProfileDto) {
        log.info("Updating user profile for ID: {}", uuid);

        User user = findUserById(uuid);

        UserProfile userProfile = userProfileRepository.findByUser(user);
        if (userProfile == null) {
            log.warn("UserProfile not found for user ID: {}, creating new profile.", uuid);
            userProfile = UserProfile.builder()
                    .user(user)
                    .build();
        }

        modelMapper.map(userProfileDto, userProfile);
        userProfileRepository.save(userProfile);
        log.info("User with ID {} updated successfully.", uuid);
    }

    @Override
    public void updateUserPassword(UUID uuid, String oldPassword, String newPassword) {

        User user = findUserById(uuid);

        if (Objects.equals(user.getPassword(), oldPassword)) {
            user.setPassword(newPassword);
        } else {
            log.info("Old password incorrect!");
            throw new IllegalArgumentException("Password incorrect!");
        }
    }

    @Override
    public void activateUser(UUID uuid) {
        User user = findUserById(uuid);
        user.setStatus(Status.ACTIVE);
        log.info("User with ID:{} status activated!", uuid);
    }

    @Override
    public void deactivateUser(UUID uuid) {
        User user = findUserById(uuid);
        user.setStatus(Status.INACTIVE);
        log.info("User with ID:{} status deactivated!", uuid);
    }

    @Override
    public void addRole(UUID uuid, UserRole role) {
        User user = findUserById(uuid);
        user.getRoles().add(role);
    }

    @Override
    public ResponseUserDto getUserById(UUID uuid) {
        log.info("Searching for a user with this ID: {}", uuid);

        User user = userRepository.findById(uuid).orElseThrow(() -> {
            log.error("User not found with these ID: {}", uuid);
            return new ResourceNotFoundException("User not found Exception!");
        });

        return userMapper.toResponse(user, ResponseUserDto.class);
    }

    @Override
    public List<ResponseUserDto> getUser(String query) {
        List<User> users = userRepository.findByFirstNameContainingIgnoreCase(query);
        if (!users.isEmpty()) {
            log.info("Users matching this name!");
            return userMapper.toResponseList(users, ResponseUserDto.class);
        } else {
            throw new ResourceNotFoundException("There is no user with this name.");
        }
    }

    @Override
    public List<ResponseUserDto> getUserByRole(Role role) {
        List<User> users = userRepository.findUsersByRole(role);
        if (!users.isEmpty()) {
            log.info("There are no users matching this ROLE: {}.", role);
            return userMapper.toResponseList(users, ResponseUserDto.class);
        }
        log.info("There is no user with this ROLE: {}", role);
        throw new ResourceNotFoundException("User not found");
    }

    public List<ResponseUserDto> getAllUsers() {
        log.info("Finding all users!");
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            log.info("Not user found!");
        }

        return userMapper.toResponseList(users, ResponseUserDto.class);
    }

    @Override
    public Long countUsers() {
        return userRepository.count();
    }

    @Override
    public void sendPasswordResetEmail(UUID uuid) {
        User user = findUserById(uuid);
        String email = user.getEmail();
        //Email service yazacam  ve sender metodu emaile password deyisme linki gedecek ve ordan deyisecek!
    }

    public void deleteUser(UUID uuid) {
        log.info("Deleting user with ID: {}", uuid);
        if (userRepository.existsById(uuid)) {
            userRepository.deleteById(uuid);
            log.info("User with ID: {} successfully deleted!", uuid);
        } else {
            log.info("Not found with these ID: {}", uuid);
            throw new ResourceNotFoundException("User not found !");
        }
    }

    public void deleteUsers() {
        log.info("Deleting all users!");
        userRepository.deleteAll();
        log.info("All users successfully deleted!");
        userRepository.resetAutoIncrement();
        log.info("User Id successfully reset!");
    }


    private User findUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> {
            log.error("User not found with ID: {}", uuid);
            return new ResourceNotFoundException("User not found");
        });
    }
}
