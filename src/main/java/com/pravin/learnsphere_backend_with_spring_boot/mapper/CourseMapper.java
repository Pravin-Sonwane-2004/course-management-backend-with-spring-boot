package com.pravin.learnsphere_backend_with_spring_boot.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseRequestDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;

public class CourseMapper {

  public static CourseResponseDTO toDTO(Course course) {
    List<String> prereqIds = course.getPrerequisites().stream()
        .map(Course::getCourseId)
        .collect(Collectors.toList());

    return new CourseResponseDTO(
        course.getCourseId(),
        course.getTitle(),
        course.getDescription(),
        prereqIds);
  }

  public static Course toEntity(CourseRequestDTO dto, List<Course> prerequisites) {
    Course course = new Course();
    course.setCourseId(dto.getCourseId());
    course.setTitle(dto.getTitle());
    course.setDescription(dto.getDescription());
    course.setPrerequisites(prerequisites);
    return course;
  }
}
