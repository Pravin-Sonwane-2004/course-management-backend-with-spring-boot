package com.pravin.learnsphere_backend_with_spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseInstanceRepository extends JpaRepository<CourseInstance, Long> {
    @Query("SELECT ci FROM CourseInstance ci " +
           "WHERE ci.year = :year AND ci.semester = :semester")
    List<CourseInstance> findByYearAndSemester(int year, int semester);
    
    @Query("SELECT ci FROM CourseInstance ci " +
           "WHERE ci.course.courseId = :courseId " +
           "AND ci.year = :year AND ci.semester = :semester")
    Optional<CourseInstance> findByCourseIdAndYearAndSemester(String courseId, int year, int semester);
    
    @Query("SELECT ci FROM CourseInstance ci " +
           "WHERE ci.course.courseId = :courseId")
    List<CourseInstance> findByCourseId(String courseId);
    
    Optional<CourseInstance> findByInstanceId(String instanceId);
    
    List<CourseInstance> findByYearAndSemesterAndCourse_Id(int year, int semester, Long courseId);
}