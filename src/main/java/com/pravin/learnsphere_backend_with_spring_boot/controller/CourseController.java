package com.pravin.learnsphere_backend_with_spring_boot.controller;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseInstanceDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@Slf4j
@Tag(name = "Course Management", description = "APIs for managing courses and their instances")
public class CourseController {
    private final CourseService courseService;
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(
        summary = "Create a new course",
        description = "Creates a new course with optional prerequisites",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Course created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Course.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Course already exists",
                content = @Content
            )
        }
    )
    @PostMapping
    public ResponseEntity<Course> createCourse(
        @Parameter(description = "Course details to create")
        @RequestBody CourseDTO courseDTO) {
        log.info("Creating new course: {}", courseDTO.getName());
        Course created = courseService.createCourse(
            courseDTO.getCourseId(), 
            courseDTO.getName(), 
            courseDTO.getDescription(), 
            courseDTO.getPrerequisites()
        );
        log.info("Successfully created course: {}", created.getName());
        return ResponseEntity.ok(created);
    }

    @Operation(
        summary = "Get all courses",
        description = "Retrieves a list of all courses with their prerequisites",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved courses",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content
            )
        }
    )
    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        
        List<CourseResponseDTO> response = courses.stream()
            .map(course -> {
                Set<String> prereqIds = course.getPrerequisites() != null 
                    ? course.getPrerequisites().stream()
                        .map(Course::getCourseId)
                        .collect(Collectors.toSet())
                    : Collections.emptySet();
                return new CourseResponseDTO(
                    course.getCourseId(),
                    course.getName(),
                    course.getDescription(),
                    prereqIds
                );
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get all course instances",
        description = "Retrieves a list of all course instances with their details",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved course instances",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseInstanceDTO.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = @Content
            )
        }
    )
    @GetMapping("/instances")
    public ResponseEntity<List<CourseInstanceDTO>> getAllInstances() {
        try {
            List<CourseInstanceDTO> instances = courseService.getAllInstances();
            return ResponseEntity.ok(instances);
        } catch (Exception e) {
            log.error("Error fetching course instances: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch course instances", e);
        }
    }

    @Operation(
        summary = "Get course by ID",
        description = "Retrieves a course by its database ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved course",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Course.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Course not found",
                content = @Content
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(
        @Parameter(description = "ID of the course to retrieve", required = true)
        @PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Get course by course ID",
        description = "Retrieves a course by its course ID (e.g., CS101)",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved course",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Course.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Course not found",
                content = @Content
            )
        }
    )
    @GetMapping("/by-course-id/{courseId}")
    public ResponseEntity<Course> getCourseByCourseId(
        @Parameter(description = "Course ID to retrieve (e.g., CS101)", required = true)
        @PathVariable String courseId) {
        return courseService.getCourseByCourseId(courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/combined")
    public ResponseEntity<List<Course>> getCombinedCourses() {
        List<Course> dbCourses = courseService.getAllCourses();
        return ResponseEntity.ok(dbCourses);
    }
}
