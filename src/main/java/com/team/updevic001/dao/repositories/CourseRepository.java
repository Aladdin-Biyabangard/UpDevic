package com.team.updevic001.dao.repositories;

import com.team.updevic001.dao.entities.Course;
import com.team.updevic001.model.enums.CourseCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> {

    //    @Query(value = "SELECT c.*, " +
//            "MATCH(c.title, c.description) AGAINST (:keyword IN BOOLEAN MODE) AS relevance " +
//            "FROM courses c " +
//            "WHERE MATCH(c.title, c.description) AGAINST (:keyword IN BOOLEAN MODE) " +
//            "ORDER BY relevance DESC " +
//            "LIMIT 50",
//            nativeQuery = true)
//    List<Course> searchCoursesByKeyword(@Param("keyword") String keyword);
//
    List<Course> findCourseByCourseCategoryType(CourseCategoryType courseCategoryType);

    List<Course> findCourseByTitle(String keyword);

}
