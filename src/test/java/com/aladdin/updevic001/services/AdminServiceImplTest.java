package com.aladdin.updevic001.services;


import com.team.updevic001.configuration.mappers.UserMapper;
import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.entities.UserRole;
import com.team.updevic001.dao.repositories.UserRepository;
import com.team.updevic001.dao.repositories.UserRoleRepository;
import com.team.updevic001.exceptions.ResourceNotFoundException;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;
import com.team.updevic001.model.enums.Status;
import com.team.updevic001.services.impl.AdminServiceImpl;
import com.team.updevic001.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceImplTest {
 /*   @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private com.team.updevic001.configuration.mappers.UserMapper userMapper;
    @Mock
    private UserServiceImpl userServiceImpl;
    @InjectMocks
    private AdminServiceImpl adminService;
    private User mockUser;
    private UserRole mockUserRole;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set up mock user
        mockUser = new User();
        mockUser.setid("123e4567-e89b-12d3-a456-426614174000");
        mockUser.setStatus(Status.INACTIVE);
        // Set up mock user role
        mockUserRole = new UserRole();
        mockUserRole.setName(Role.ADMIN);
        mockUser.setRoles(new ArrayList<>(Arrays.asList(mockUserRole)));
        when(userServiceImpl.findUserById(anyString())).thenReturn(mockUser);
    }
    @Test
    public void testActivateUser(){
        when(userRepository.findById(anyString())).thenReturn(Optional.of(mockUser));
        adminService.activateUser("123e4567-e89b-12d3-a456-426614174000");
        assertEquals(Status.ACTIVE,mockUser.getStatus());
        verify(userRepository,times(1)).save(mockUser);

    }
    @Test
    public void testDeactivateUser(){
        //Mock davranisi
        when(userRepository.findById(anyString())).thenReturn(Optional.of(mockUser));
        adminService.deactivateUser("123e4567-e89b-12d3-a456-426614174000");
        assertEquals(Status.INACTIVE,mockUser.getStatus());
        verify(userRepository,times(1)).save(mockUser);
    }
    @Test
    public void testAssignRoleToUser() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(mockUser));
        adminService.assignRoleToUser("123e4567-e89b-12d3-a456-426614174000", Role.ADMIN);
        verify(userRoleRepository, times(1)).save(any(UserRole.class));
        verify(userRepository, times(1)).save(mockUser);
    }
    @Test
    public void testRemoveRoleFromUser() {
        when(userServiceImpl.findUserById(anyString())).thenReturn(mockUser);
        adminService.removeRoleFromUser("123e4567-e89b-12d3-a456-426614174000", Role.ADMIN);
        assertTrue(mockUser.getRoles().isEmpty());

    }
    @Test
    public void testGetAllUsers(){
        when(userRepository.findUsersByRole(Role.ADMIN)).thenReturn(Arrays.asList(mockUser));
        when(userMapper.toResponseList(anyList(),eq(ResponseUserDto.class)))
        .thenReturn(Arrays.asList(new ResponseUserDto()));
        List<ResponseUserDto> users=adminService.getUsersByRole(Role.ADMIN);
        assertNotNull(users);
        assertEquals(1,users.size());
    }
    @Test
    public void testCountUsers(){
        when(userRepository.count()).thenReturn(5L);
        Long count=adminService.countUsers();
        assertEquals(5L,count);
    }
    @Test
    public void testDeleteUsers(){
        doNothing().when(userRepository).deleteAll();
        adminService.deleteUsers();
        verify(userRepository,times(1)).deleteAll();
//        verify(userRepository,times(1)).resetAutoIncrement();
    }
    @Test
    public void testFindUserByIdNotFound(){
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()-> {
            adminService.activateUser("non-existent-id");
        });
        assertEquals("User not found",exception.getMessage());
    }
*/
    }

