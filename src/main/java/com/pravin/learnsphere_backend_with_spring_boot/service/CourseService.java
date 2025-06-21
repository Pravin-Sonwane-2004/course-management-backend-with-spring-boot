package com.pravin.learnsphere_backend_with_spring_boot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseRequestDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.BadRequestException;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.ConflictException;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.NotFoundException;
import com.pravin.learnsphere_backend_with_spring_boot.mapper.CourseMapper;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseRepository;

@Service
public class CourseService {

  @Autowired
  private CourseRepository courseRepo;

  public CourseResponseDTO createCourse(CourseRequestDTO dto) {
    List<Course> prerequisites = dto.getPrerequisites().stream()
        .map(id -> courseRepo.findByCourseId(id)
            .orElseThrow(() -> new BadRequestException("Prerequisite not found: " + id)))
        .collect(Collectors.toList());

    Course course = CourseMapper.toEntity(dto, prerequisites);
    courseRepo.save(course);

    return CourseMapper.toDTO(course);
  }

  public List<CourseResponseDTO> getAllCourses() {
    return courseRepo.findAll().stream()
        .map(CourseMapper::toDTO)
        .collect(Collectors.toList());
  }

  public CourseResponseDTO getCourse(String courseId) {
    Course course = courseRepo.findByCourseId(courseId)
        .orElseThrow(() -> new NotFoundException("Course not found"));
    return CourseMapper.toDTO(course);
  }

  public void deleteCourse(String courseId) {
    Course course = courseRepo.findByCourseId(courseId)
        .orElseThrow(() -> new NotFoundException("Course not found"));

    if (courseRepo.existsByPrerequisites(course)) {
      throw new ConflictException("Cannot delete. This course is a prerequisite for other courses.");
    }

    courseRepo.delete(course);
  }
}
