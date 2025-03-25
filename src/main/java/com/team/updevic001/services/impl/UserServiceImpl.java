package com.team.updevic001.services.impl;

import com.team.updevic001.configuration.mappers.UserMapper;
import com.team.updevic001.dao.entities.Student;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.entities.UserProfile;
import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.dao.repositories.StudentRepository;
import com.team.updevic001.dao.repositories.UserProfileRepository;
import com.team.updevic001.dao.repositories.UserRepository;
import com.team.updevic001.dao.repositories.UserRoleRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
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
    private final StudentRepository studentRepository;
    private final UserRoleRepository userRoleRepository;


    @Transactional
    public ResponseUserDto newUser(StudentDto user) {
        log.info("Creating a new user with email: {}", user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Email {} already exists!", user.getEmail());
            throw new IllegalArgumentException("Email already exists!");
        }
        //PASSWORD ENCODED will be used here

        Student student = modelMapper.map(user, Student.class);

        studentRepository.save(student);


        UserProfile userProfile = UserProfile.builder()
                .user(student)
                .build();


        UserRole userRole = userRoleRepository.findByName(Role.STUDENT).orElseGet(() -> {
            UserRole role = UserRole.builder()
                    .name(Role.STUDENT)
                    .build();
            return userRoleRepository.save(role);

        });

        student.getRoles().add(userRole);

        userRepository.save(student);
        userProfileRepository.save(userProfile);


        log.info("User created successfully with ID: {}", student.getUuid());
        //  confirmationEmailServiceImpl.sendEmail(user.getEmail(), "Thank you for registering", "Your token");
        return userMapper.toResponse(student, ResponseUserDto.class);
    }


    public void updateUserProfileInfo(String uuid, UserProfileDto userProfileDto) {
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
    public void updateUserPassword(String uuid, String oldPassword, String newPassword) {

        User user = findUserById(uuid);

        if (Objects.equals(user.getPassword(), oldPassword)) {
            //PASSWORD ENCODED will be used here
            user.setPassword(newPassword);
            userRepository.save(user);
        } else {
            log.info("Old password incorrect!");
            throw new IllegalArgumentException("Password incorrect!");
        }
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
        log.info("Deleting user with ID: {}", uuid);
        if (userRepository.existsById(uuid)) {
            userRepository.deleteById(uuid);
            log.info("User with ID: {} successfully deleted!", uuid);
        } else {
            log.info("Not found with these ID: {}", uuid);
            throw new ResourceNotFoundException("User not found !");
        }
    }

    public User findUserById(String uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> {
            log.error("User not found with ID: {}", uuid);
            return new ResourceNotFoundException("User not found");
        });
    }
}
