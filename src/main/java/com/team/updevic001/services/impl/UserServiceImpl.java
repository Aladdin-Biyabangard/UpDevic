package com.team.updevic001.services.impl;

import com.team.updevic001.config.mappers.UserMapper;
import com.team.updevic001.dao.entities.Teacher;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.entities.UserProfile;
import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.dao.repositories.TeacherRepository;
import com.team.updevic001.dao.repositories.UserProfileRepository;
import com.team.updevic001.dao.repositories.UserRepository;
import com.team.updevic001.dao.repositories.UserRoleRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.request.TeacherDto;
import com.team.updevic001.model.dtos.request.UserProfileDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserProfileRepository userProfileRepository;
    private final UserRoleRepository userRoleRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public ResponseUserDto newUser(TeacherDto teacherDto) {
        log.info("Creating a new user with email: {}", teacherDto.getEmail());

        if (userRepository.existsByEmail(teacherDto.getEmail())) {
            log.warn("Email {} already exists!", teacherDto.getEmail());
            throw new IllegalArgumentException("Email already exists!");
        }

        Teacher teacher = modelMapper.map(teacherDto, Teacher.class);

        teacherRepository.save(teacher);


        UserProfile userProfile = UserProfile.builder()
                .user(teacher)
                .build();


        UserRole role = UserRole.builder()
                .name(Role.STUDENT)
                .build();
        teacher.getRoles().add(role);


        userRepository.save(teacher);
        userProfileRepository.save(userProfile);


        log.info("User created successfully with ID: {}", teacher.getUuid());
        return userMapper.toResponse(teacher, ResponseUserDto.class);
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
            user.setPassword(newPassword);
            userRepository.save(user);
        } else {
            log.info("Old password incorrect!");
            throw new IllegalArgumentException("Password incorrect!");
        }
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
    public void addRole(String uuid, UserRole role) {
        User user = findUserById(uuid);
        user.getRoles().add(role);
        userRepository.save(user);
        userRoleRepository.saveAll(user.getRoles());
        log.info("New rol successfully added!");
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
    public List<ResponseUserDto> getUserByRole(Role role) {
        List<User> users = userRepository.findUsersByRole(role);
        if (!users.isEmpty()) {
            log.info("There are no users matching this ROLE: {}.", role);
            return userMapper.toResponseList(users, ResponseUserDto.class);
        }
        log.info("There is no user with this ROLE: {}", role);
        throw new ResourceNotFoundException("User not found");
    }

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
    public Long countUsers() {
        return userRepository.count();
    }

    @Override
    public void sendPasswordResetEmail(String uuid) {
        User user = findUserById(uuid);
        String email = user.getEmail();
        //Email service yazacam  ve sender metodu emaile password deyisme linki gedecek ve ordan deyisecek!
    }

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

    public void deleteUsers() {
        log.info("Deleting all users!");
        userRepository.deleteAll();
        log.info("All users successfully deleted!");
        userRepository.resetAutoIncrement();
        log.info("User Id successfully reset!");
    }


    private User findUserById(String uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> {
            log.error("User not found with ID: {}", uuid);
            return new ResourceNotFoundException("User not found");
        });
    }
}
