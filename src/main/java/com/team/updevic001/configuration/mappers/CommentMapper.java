package com.team.updevic001.configuration.mappers;

import com.team.updevic001.dao.entities.Comment;
import com.team.updevic001.model.dtos.response.comment.ResponseCommentDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final ModelMapper modelMapper;


    public ResponseCommentDto toDto(Comment comment) {
        return modelMapper.map(comment, ResponseCommentDto.class);
    }

    public List<ResponseCommentDto> toDto(List<Comment> comments) {
        return comments.stream().map(this::toDto).toList();
    }
}
