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

import com.pravin.learnsphere_backend_with_spring_boot.dto.InstanceRequestDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.InstanceResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseInstanceService;

@RestController
@RequestMapping("/api/instances")
public class CourseInstanceController {
  @Autowired
  private CourseInstanceService service;

  @PostMapping
  public ResponseEntity<InstanceResponseDTO> createInstance(@RequestBody InstanceRequestDTO dto) {
    return new ResponseEntity<>(service.createInstance(dto), HttpStatus.CREATED);
  }

  @GetMapping("/{year}/{semester}")
  public List<InstanceResponseDTO> listBySemester(@PathVariable int year, @PathVariable int semester) {
    return service.listInstances(year, semester);
  }

  @GetMapping("/{year}/{semester}/{courseId}")
  public InstanceResponseDTO getInstance(@PathVariable int year, @PathVariable int semester,
      @PathVariable String courseId) {
    return service.getInstance(year, semester, courseId);
  }

  @DeleteMapping("/{year}/{semester}/{courseId}")
  public ResponseEntity<Void> deleteInstance(@PathVariable int year, @PathVariable int semester,
      @PathVariable String courseId) {
    service.deleteInstance(year, semester, courseId);
    return ResponseEntity.noContent().build();
  }
}
