package com.pravin.learnsphere_backend_with_spring_boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseId(String courseId);
    
    @Query("SELECT c FROM Course c " +
           "LEFT JOIN FETCH c.prerequisites " +
           "WHERE c.id = :id")
    Optional<Course> findByIdWithPrerequisites(Long id);
    
    @Query("SELECT c FROM Course c " +
            "LEFT JOIN FETCH c.prerequisites p " +
            "LEFT JOIN FETCH c.instances " +
            "WHERE c.id = c.id")
    List<Course> findAllWithDetails();
    
    @Query("SELECT c FROM Course c " +
           "LEFT JOIN FETCH c.prerequisites " +
           "WHERE c.courseId = :courseId")
    Optional<Course> findByCourseIdWithPrerequisites(String courseId);
}