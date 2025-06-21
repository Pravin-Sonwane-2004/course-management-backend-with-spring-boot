package com.pravin.learnsphere_backend_with_spring_boot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseRequestDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
  @Autowired
  private CourseService service;

  @PostMapping
  public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO dto) {
    return new ResponseEntity<>(service.createCourse(dto), HttpStatus.CREATED);
  }

  @GetMapping
  public List<CourseResponseDTO> getAllCourses() {
    return service.getAllCourses();
  }

  @GetMapping("/{courseId}")
  public CourseResponseDTO getCourse(@PathVariable String courseId) {
    return service.getCourse(courseId);
  }

  @DeleteMapping("/{courseId}")
  public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
    service.deleteCourse(courseId);
    return ResponseEntity.noContent().build();
  }
}
