package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
