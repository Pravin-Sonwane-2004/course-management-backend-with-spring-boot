package com.pravin.learnsphere_backend_with_spring_boot.controller;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseDTO courseDTO) {
        if (courseDTO.getPrerequisites() != null) {
            courseDTO.setPrerequisites(new HashSet<>(courseDTO.getPrerequisites()));
        }
        Course created = courseService.createCourse(
            courseDTO.getCourseId(), 
            courseDTO.getName(), 
            courseDTO.getDescription(), null
        );
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/combined")
    public ResponseEntity<List<Course>> getCombinedCourses() {
        List<Course> dbCourses = courseService.getAllCourses();
        return ResponseEntity.ok(dbCourses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-course-id/{courseId}")
    public ResponseEntity<Course> getCourseByCourseId(@PathVariable String courseId) {
        return courseService.getCourseByCourseId(courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
