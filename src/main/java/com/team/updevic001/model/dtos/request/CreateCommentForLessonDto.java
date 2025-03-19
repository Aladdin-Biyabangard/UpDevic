package com.team.updevic001.model.dtos.request;

import com.team.updevic001.dao.entities.Lesson;
import com.team.updevic001.dao.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentForLessonDto {

    private String content;

    private User user;

    private Lesson lesson;
}
