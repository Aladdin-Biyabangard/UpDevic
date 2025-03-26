package com.team.updevic001.services.impl;

import com.team.updevic001.config.mappers.UserMapper;
import com.team.updevic001.dao.entities.Student;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.entities.UserProfile;
import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.dao.repositories.*;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.mail.ConfirmationEmailServiceImpl;
import com.team.updevic001.model.dtos.request.StudentDto;
import com.team.updevic001.model.dtos.request.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthHelper authHelper;


    public void updateUserProfileInfo(String uuid, UserProfileDto userProfileDto) {
        log.info("Updating user profile for ID: {}", uuid);
        User authenticatedUser = authHelper.validateUserAccess(uuid);

        UserProfile userProfile = userProfileRepository.findByUser(authenticatedUser);
        if (userProfile == null) {
            log.warn("UserProfile not found for user ID: {}, creating new profile.", uuid);
            userProfile = UserProfile.builder()
                    .user(authenticatedUser)
                    .build();
        }

        modelMapper.map(userProfileDto, userProfile);
        userProfileRepository.save(userProfile);
        log.info("User with ID {} updated successfully.", uuid);
    }

    @Override
    public void updateUserPassword(String uuid, ChangePasswordDto passwordDto) {
        User authenticatedUser = authHelper.validateUserAccess(uuid);
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


    // BU adi bildiyimiz restPassword emaili olacaq

//    @Override
//    public void sendPasswordResetEmail(String email) {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isPresent()) {
//            confirmationEmailServiceImpl.sendEmail(optionalUser.get().getEmail(), "Click to change password", "Your token and RESTApi for change password");
//            log.info("Email successfully send!");
//        } else {
//            throw new ResourceNotFoundException("User not found");
//        }
//    }

    /* Token generatorda uniq olan email oldugu ucun subject emaille olacaq.
       Ona gorede extract token ve validate token email uzerine olacaq
       Controllerde token qebul eden metod ise request paramla gelen tokeni
       extract token ve validate token metodlari ile yoxladiqdan sonra bu metodu cagiracaq
    */

    public void resetPassword(String email, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            //PASSWORD ENCODED will be used here

            optionalUser.get().setPassword(newPassword);
            log.info("Password changed correctly!");
        }
    }

    @Override
    public void deleteUser(String uuid) {
        log.info("Attempting to delete user with ID: {}", uuid);

        if (!userRepository.existsById(uuid)) {
            log.error("User with ID: {} not found", uuid);
            throw new ResourceNotFoundException("USER_NOT_FOUND");
        }

        User authenticatedUser = authHelper.validateUserAccess(uuid);

        userRepository.delete(authenticatedUser);
        log.info("User with ID: {} successfully deleted.", uuid);
    }


    public User findUserById(String uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> {
            log.error("User not found with ID: {}", uuid);
            return new ResourceNotFoundException("User not found");
        });
    }
}
