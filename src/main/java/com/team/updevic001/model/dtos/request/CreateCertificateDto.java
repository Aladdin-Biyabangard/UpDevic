package com.team.updevic001.model.dtos.request;

import com.team.updevic001.dao.entities.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCertificateDto {

    private String certificateContent;

    private Course course;
}
