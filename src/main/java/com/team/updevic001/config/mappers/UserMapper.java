package com.team.updevic001.config.mappers;

import com.team.updevic001.model.dtos.response.teacher.ResponseTeacherDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public <D, E> E toEntity(D dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public <E, D> D toResponse(E entity, Class<D> dtoClass) {
        D dto = modelMapper.map(entity, dtoClass);

        // Əgər ResponseUserDto-dan extend edən bir DTO-dursa, burada əlavə dəyişikliklər edirik
        if (dto instanceof ResponseTeacherDto responseTeacherDto) {
            responseTeacherDto.setHireDate(LocalDateTime.now());
        }

        return dto;
    }

    public <D, E> List<E> toEntityList(List<D> dtoList, Class<E> entityClass) {
        return dtoList.stream()
                .map(dto -> toEntity(dto, entityClass))
                .toList();
    }

    public <E, D> List<D> toResponseList(List<E> entityList, Class<D> dtoClass) {
        return entityList.stream()
                .map(entity -> toResponse(entity, dtoClass))
                .toList();
    }


}
