package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, String> {

    @Query("SELECT cc FROM CourseCategory cc WHERE " +
            "LOWER(cc.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CourseCategory> searchCategoryByKeyword(@Param("keyword") String keyword);
}
