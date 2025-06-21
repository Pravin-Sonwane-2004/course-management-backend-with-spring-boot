package com.pravin.learnsphere_backend_with_spring_boot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseRequestDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

  @Autowired
  private CourseService courseService;

  
  @PostMapping("/add")
  public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO dto) {
    CourseResponseDTO response = courseService.createCourse(dto);
    return ResponseEntity.status(201).body(response);
  }

  // Get all courses
  @GetMapping("/all")
  public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
    return ResponseEntity.ok(courseService.getAllCourses());
  }

  // Get a specific course
  @GetMapping("/{courseId}")
  public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable String courseId) {
    return ResponseEntity.ok(courseService.getCourse(courseId));
  }

  // Delete a course
  @DeleteMapping("/{courseId}")
  public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
    courseService.deleteCourse(courseId);
    return ResponseEntity.noContent().build();
  }
}
