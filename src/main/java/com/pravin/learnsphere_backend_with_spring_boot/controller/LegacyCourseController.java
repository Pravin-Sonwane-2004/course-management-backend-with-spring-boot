package com.pravin.learnsphere_backend_with_spring_boot.controller;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LegacyCourseController {
    private final CourseService courseService;

    @Autowired
    public LegacyCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/get-all-courses")
    public ResponseEntity<List<Course>> getAllCoursesLegacy() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
}
