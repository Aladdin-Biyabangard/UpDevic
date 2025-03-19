package com.team.updevic001.model.dtos.request;

import com.team.updevic001.dao.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentCertificateDto {
    private String certificateContent;

    private Student student;
}
