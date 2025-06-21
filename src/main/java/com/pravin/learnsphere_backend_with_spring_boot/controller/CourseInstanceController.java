package com.pravin.learnsphere_backend_with_spring_boot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pravin.learnsphere_backend_with_spring_boot.dto.InstanceRequestDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.InstanceResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseInstanceService;

@RestController
@RequestMapping("/api/instances")
public class CourseInstanceController {

  @Autowired
  private CourseInstanceService instanceService;

  // Create a course instance
  @PostMapping
  public ResponseEntity<InstanceResponseDTO> createInstance(@RequestBody InstanceRequestDTO dto) {
    InstanceResponseDTO response = instanceService.createInstance(dto);
    return ResponseEntity.status(201).body(response);
  }

  // List all instances for year and semester
  @GetMapping("/{year}/{semester}")
  public ResponseEntity<List<InstanceResponseDTO>> listInstances(
      @PathVariable int year,
      @PathVariable int semester) {
    return ResponseEntity.ok(instanceService.listInstances(year, semester));
  }

  // Get a specific course instance
  @GetMapping("/{year}/{semester}/{courseId}")
  public ResponseEntity<InstanceResponseDTO> getInstance(
      @PathVariable int year,
      @PathVariable int semester,
      @PathVariable String courseId) {
    return ResponseEntity.ok(instanceService.getInstance(year, semester, courseId));
  }

  // Delete a specific course instance
  @DeleteMapping("/{year}/{semester}/{courseId}")
  public ResponseEntity<Void> deleteInstance(
      @PathVariable int year,
      @PathVariable int semester,
      @PathVariable String courseId) {
    instanceService.deleteInstance(year, semester, courseId);
    return ResponseEntity.noContent().build();
  }
}
