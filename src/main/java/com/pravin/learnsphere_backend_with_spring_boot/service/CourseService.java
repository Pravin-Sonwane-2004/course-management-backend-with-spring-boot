package com.pravin.learnsphere_backend_with_spring_boot.service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseRequestDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseRepository;

@Service
public class CourseService {
  @Autowired
  private CourseRepository courseRepo;

  public CourseResponseDTO createCourse(CourseRequestDTO dto) {
    List<Course> prerequisites = dto.prerequisites().stream()
        .map(id -> courseRepo.findByCourseId(id)
            .orElseThrow(() -> new BadRequestException("Prerequisite not found: " + id)))
        .toList();

    Course course = new Course();
    course.setCourseId(dto.courseId());
    course.setTitle(dto.title());
    course.setDescription(dto.description());
    course.setPrerequisites(prerequisites);

    courseRepo.save(course);

    return mapToDTO(course);
  }

  public List<CourseResponseDTO> getAllCourses() {
    return courseRepo.findAll().stream()
        .map(this::mapToDTO)
        .toList();
  }

  public CourseResponseDTO getCourse(String courseId) {
    Course course = courseRepo.findByCourseId(courseId)
        .orElseThrow(() -> new NotFoundException("Course not found"));
    return mapToDTO(course);
  }

  public void deleteCourse(String courseId) {
    Course course = courseRepo.findByCourseId(courseId)
        .orElseThrow(() -> new NotFoundException("Course not found"));

    if (courseRepo.existsByPrerequisites(course)) {
      throw new ConflictException("Cannot delete. This course is a prerequisite.");
    }

    courseRepo.delete(course);
  }

  private CourseResponseDTO mapToDTO(Course course) {
    List<String> prereqIds = course.getPrerequisites().stream()
        .map(Course::getCourseId)
        .toList();
    return new CourseResponseDTO(course.getCourseId(), course.getTitle(), course.getDescription(), prereqIds);
  }
}
