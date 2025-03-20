package com.team.updevic001.model.dtos.response.student;

import com.team.updevic001.model.dtos.response.certificate.ResponseCertificateDto;
import com.team.updevic001.model.dtos.response.course.ResponseCourseShortInfoDto;
import com.team.updevic001.model.dtos.response.user.ResponseUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStudentWithCoursesDto extends ResponseUserDto {

    private String studentNumber;
    private ResponseCertificateDto certificateDto;
    private List<ResponseCourseShortInfoDto> courseDtoS;
}
