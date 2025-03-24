package com.team.updevic001.config.mappers;

import com.team.updevic001.dao.entities.CourseCategory;
import com.team.updevic001.model.dtos.response.course.ResponseCategoryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Data
public class CategoryMapper {

    private final CourseMapper courseMapper;
    private final ModelMapper modelMapper;


    public ResponseCategoryDto toDto(CourseCategory courseCategory) {
        return new ResponseCategoryDto(
                courseCategory.getCategory(),
                Optional.ofNullable(courseMapper.courseShortInfoDto(courseCategory.getCourses()))
                        .orElse(Collections.emptyList()),
                courseCount(courseCategory)
        );
    }


    private Integer courseCount(CourseCategory courseCategory) {
        return courseCategory.getCourses() != null ? courseCategory.getCourses().size() : 0;
    }
}
