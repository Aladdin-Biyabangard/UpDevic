package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Comment;
import com.team.updevic001.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, String> {

    Optional<Comment> findCommentByIdAndUser(String commentId, User user);
}
