package com.pravin.learnsphere_backend_with_spring_boot.repository;


import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseInstanceRepository extends JpaRepository<CourseInstance, Long> {
    List<CourseInstance> findByYearAndSemester(int year, String semester);
    List<CourseInstance> findByYearAndSemesterAndCourse_Id(int year, String semester, Long courseId);
}