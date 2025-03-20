package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.User;
import com.team.updevic001.dao.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    UserProfile findByUser(User user);

}
