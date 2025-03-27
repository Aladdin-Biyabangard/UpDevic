package com.team.updevic001.services.impl;

import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.entities.UserProfile;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.ChangePasswordDto;
import com.team.updevic001.model.dtos.request.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.services.UserService;
import com.team.updevic001.utility.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final com.team.updevic001.configuration.mappers.UserMapper userMapper;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthHelper authHelper;


    public void updateUserProfileInfo(UserProfileDto userProfileDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Updating user profile for ID: {}", authenticatedUser.getUuid());

        UserProfile userProfile = userProfileRepository.findByUser(authenticatedUser);
        if (userProfile == null) {
            log.warn("UserProfile not found for user ID: {}, creating new profile.", authenticatedUser.getUuid());
            userProfile = UserProfile.builder()
                    .user(authenticatedUser)
                    .build();
        }
        modelMapper.map(userProfileDto, userProfile);
        userProfileRepository.save(userProfile);
        log.info("User with ID {} updated successfully.", authenticatedUser.getUuid());
    }


    @Override
    public void updateUserPassword(ChangePasswordDto passwordDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), authenticatedUser.getPassword()) || !passwordDto.getNewPassword().equals(passwordDto.getRetryPassword())) {
            log.error("Old password incorrect or retry password mismatches");
            throw new IllegalArgumentException("Password incorrect!");
        }
        authenticatedUser.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(authenticatedUser);
    }

    @Override
    public ResponseUserDto getUserById(String uuid) {
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
    public void deleteUser() {
        User authenticatedUser = authHelper.getAuthenticatedUser();

        log.info("Attempting to delete user with ID: {}", authenticatedUser.getUuid());

        userRepository.delete(authenticatedUser);
        log.info("User successfully deleted.");
    }


    public User findUserById(String uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> {
            log.error("User not found with ID: {}", uuid);
            return new ResourceNotFoundException("User not found");
        });
    }
}
