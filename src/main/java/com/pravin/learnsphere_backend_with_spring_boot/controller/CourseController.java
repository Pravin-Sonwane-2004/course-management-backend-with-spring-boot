package com.pravin.learnsphere_backend_with_spring_boot.controller;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.exception.BadRequestException;
import com.pravin.learnsphere_backend_with_spring_boot.exception.ConflictException;
import com.pravin.learnsphere_backend_with_spring_boot.exception.NotFoundException;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseRepository;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Tag(name = "Course Management", description = "APIs for managing courses and their instances")
public class CourseController {
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseService courseService, CourseRepository courseRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
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
        try {
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
        } catch (Exception e) {
            log.error("Error fetching courses: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.emptyList());
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

    @Operation(
        summary = "Delete a course",
        description = "Deletes a course and its instances. Course must not have active instances.",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Course deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Course not found",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Cannot delete course with active instances",
                content = @Content
            )
        }
    )
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
        @Parameter(description = "Course ID to delete (e.g., CS101)", required = true)
        @PathVariable String courseId) {
        try {
            log.info("Deleting course with ID: {}", courseId);
            
            Optional<Course> course = courseRepository.findByCourseIdWithPrerequisites(courseId);
            if (!course.isPresent()) {
                log.warn("Course not found for deletion: {}", courseId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            courseService.deleteCourse(courseId);
            log.info("Successfully deleted course: {}", courseId);
            return ResponseEntity.noContent().build();

        } catch (BadRequestException e) {
            log.error("Bad request while deleting course: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NotFoundException e) {
            log.warn("Course not found during deletion: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ConflictException e) {
            log.error("Conflict while deleting course: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            log.error("Unexpected error while deleting course: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/combined")
    public ResponseEntity<List<Course>> getCombinedCourses() {
        List<Course> dbCourses = courseService.getAllCourses();
        return ResponseEntity.ok(dbCourses);
    }
}
