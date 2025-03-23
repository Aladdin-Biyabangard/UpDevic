package com.team.updevic001.services;

import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import com.team.updevic001.model.enums.Role;

import java.util.List;

public interface AdminService {

    List<ResponseUserDto> getAllUsers();

    void assignRoleToUser(String userId, Role role);  // İstifadəçiyə rol təyin etmək

    void removeRoleFromUser(String userId, Role role);  // İstifadəçidən rol silmək

    List<ResponseUserDto> getUsersByRole(Role role);  // Rol üzrə istifadəçiləri almaq

    void activateUser(String uuid);

    void deactivateUser(String uuid);

    Long countUsers();

    void deleteUsers();


}
