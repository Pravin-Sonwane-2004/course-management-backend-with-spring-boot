package com.pravin.learnsphere_backend_with_spring_boot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;

public interface CourseInstanceRepository extends JpaRepository<CourseInstance, Long> {
  List<CourseInstance> findByYearAndSemester(int year, int semester);

  Optional<CourseInstance> findByYearAndSemesterAndCourse_CourseId(int year, int semester, String courseId);

  void deleteByYearAndSemesterAndCourse_CourseId(int year, int semester, String courseId);
}
