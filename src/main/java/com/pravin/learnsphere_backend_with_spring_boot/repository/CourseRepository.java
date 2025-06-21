package com.pravin.learnsphere_backend_with_spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
  Optional<Course> findByCourseId(String courseId);

  boolean existsByPrerequisites(Course course);
}
