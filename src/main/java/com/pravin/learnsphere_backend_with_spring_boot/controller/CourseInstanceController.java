package com.pravin.learnsphere_backend_with_spring_boot.controller;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseInstanceDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;
import com.pravin.learnsphere_backend_with_spring_boot.exception.ResourceNotFoundException;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseInstanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/api/instances")
@Tag(name = "Course Instances", description = "APIs for managing course instances")
@Slf4j
@Validated
public class CourseInstanceController {

  private final CourseInstanceService courseInstanceService;

  @Autowired
  public CourseInstanceController(CourseInstanceService courseInstanceService) {
    this.courseInstanceService = courseInstanceService;
  }

  @Operation(summary = "Create a new course instance")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Course instance created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseInstanceDTO.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @PostMapping
  public ResponseEntity<CourseInstanceDTO> createInstance(@RequestBody CourseInstanceDTO dto) {
    if (dto.getCourseId() == null
        || dto.getYear() == null || dto.getYear() == 0
        || dto.getSemester() == null || dto.getSemester() == 0) {
      throw new IllegalArgumentException("Course ID, year, and semester are required");
    }

    CourseInstance created = courseInstanceService.createInstance(
        dto.getCourseId(), dto.getYear(), dto.getSemester());

    CourseInstanceDTO responseDto = new CourseInstanceDTO();
    responseDto.setInstanceId(created.getInstanceId());
    responseDto.setCourseId(created.getCourse().getCourseId());
    responseDto.setYear(created.getYear());
    responseDto.setSemester(created.getSemester());
    responseDto.setCourseName(created.getCourse().getName());
    responseDto.setCourseDescription(created.getCourse().getDescription());
    responseDto.setCoursePrerequisites(
        created.getCourse().getPrerequisites() != null
            ? created.getCourse().getPrerequisites().stream().map(p -> p.getCourseId()).toArray(String[]::new)
            : new String[0]);

    log.info("Created course instance: {}", responseDto.getInstanceId());
    return ResponseEntity.ok(responseDto);
  }

  @Operation(summary = "Get course instances for a specific year and semester")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved course instances", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseInstanceDTO[].class))),
      @ApiResponse(responseCode = "400", description = "Invalid year or semester", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @GetMapping("/{year}/{semester}")
  public ResponseEntity<CourseInstanceDTO[]> getInstancesByYearAndSemester(
      @PathVariable int year,
      @PathVariable int semester) {

    if (year < 1900 || year > 2100 || semester < 1 || semester > 4) {
      throw new IllegalArgumentException("Invalid year or semester values");
    }

    List<CourseInstance> instances = courseInstanceService.getInstancesByYearAndSemester(year, semester);
    CourseInstanceDTO[] dtos = instances.stream().map(instance -> {
      CourseInstanceDTO dto = new CourseInstanceDTO();
      dto.setInstanceId(instance.getInstanceId());
      dto.setCourseId(instance.getCourse().getCourseId());
      dto.setYear(instance.getYear());
      dto.setSemester(instance.getSemester());
      dto.setCourseName(instance.getCourse().getName());
      dto.setCourseDescription(instance.getCourse().getDescription());
      dto.setCoursePrerequisites(
          instance.getCourse().getPrerequisites() != null
              ? instance.getCourse().getPrerequisites().stream().map(p -> p.getCourseId()).toArray(String[]::new)
              : new String[0]);
      return dto;
    }).toArray(CourseInstanceDTO[]::new);

    log.info("Retrieved {} course instances for year {} semester {}", dtos.length, year, semester);
    return ResponseEntity.ok(dtos);
  }

  @Operation(summary = "Get all course instances")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all course instances", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseInstanceDTO[].class))),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @GetMapping
  public ResponseEntity<CourseInstanceDTO[]> getAllInstances() {
    List<CourseInstanceDTO> dtos = courseInstanceService.getAllInstances();
    log.info("Retrieved {} course instances", dtos.size());
    return ResponseEntity.ok(dtos.toArray(new CourseInstanceDTO[0]));
  }

  @Operation(summary = "Get a specific course instance")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved course instance", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseInstance.class))),
      @ApiResponse(responseCode = "404", description = "Course instance not found", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @GetMapping("/{year}/{semester}/{courseId}")
  public ResponseEntity<CourseInstance> getInstanceByCourseId(
      @PathVariable int year,
      @PathVariable int semester,
      @PathVariable String courseId) {

    if (year < 1900 || year > 2100 || semester < 1 || semester > 4) {
      throw new IllegalArgumentException("Invalid year or semester values");
    }

    return courseInstanceService
        .getInstanceByCourseIdAndYearAndSemester(courseId, year, semester)
        .map(instance -> {
          log.info("Retrieved course instance: {}", instance.getInstanceId());
          return ResponseEntity.ok(instance);
        })
        .orElseThrow(() -> new ResourceNotFoundException(
            "Course instance not found for courseId: " + courseId +
                ", year: " + year + ", semester: " + semester));
  }

  @Operation(summary = "Delete a course instance")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Course instance deleted successfully", content = @Content),
      @ApiResponse(responseCode = "404", description = "Course instance not found", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @DeleteMapping("/{year}/{semester}/{courseId}")
  public ResponseEntity<Void> deleteInstance(
      @PathVariable int year,
      @PathVariable int semester,
      @PathVariable String courseId) {

    if (year < 1900 || year > 2100 || semester < 1 || semester > 4) {
      throw new IllegalArgumentException("Invalid year or semester values");
    }

    courseInstanceService.deleteInstanceByCourseIdAndYearAndSemester(courseId, year, semester);
    log.info("Deleted course instance: {}_{}_{}", courseId, year, semester);
    return ResponseEntity.noContent().build();
  }
}
