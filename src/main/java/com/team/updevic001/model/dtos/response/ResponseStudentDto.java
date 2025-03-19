package com.team.updevic001.model.dtos.response;

import com.team.updevic001.dao.entities.Certificate;
import com.team.updevic001.model.dtos.request.AddStudentToCourseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStudentDto extends ResponseUserDto {

    private String studentNumber;

    private LocalDateTime enrolledDate;

    private Certificate certificate;

}
