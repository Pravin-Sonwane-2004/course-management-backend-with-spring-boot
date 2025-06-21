package com.pravin.learnsphere_backend_with_spring_boot.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);
}