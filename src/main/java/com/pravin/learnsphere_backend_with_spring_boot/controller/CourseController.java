package com.pravin.learnsphere_backend_with_spring_boot.controller;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.BadRequestException;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.NotFoundException;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.ConflictException;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseRepository;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.HashSet;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseService courseService, CourseRepository courseRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseDTO courseDTO) {
        Course created = courseService.createCourse(
            courseDTO.getCourseId(), 
            courseDTO.getName(), 
            courseDTO.getDescription(), 
            courseDTO.getPrerequisites()
        );
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            if (courses == null) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            
            List<CourseResponseDTO> response = courses.stream()
                .map(course -> {
                    Set<String> prereqIds = new HashSet<>();
                    if (course.getPrerequisites() != null) {
                        course.getPrerequisites().forEach(prereq -> {
                            if (prereq != null) {
                                prereqIds.add(prereq.getCourseId());
                            }
                        });
                    }
                    return CourseResponseDTO.builder()
                        .courseId(course.getCourseId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .prerequisites(prereqIds)
                        .build();
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.emptyList());
        }
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

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseId) {
        try {
            // First check if course exists
            Optional<Course> course = courseRepository.findByCourseIdWithPrerequisites(courseId);
            if (!course.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Try to delete the course
            courseService.deleteCourse(courseId);
            return ResponseEntity.noContent().build();

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
