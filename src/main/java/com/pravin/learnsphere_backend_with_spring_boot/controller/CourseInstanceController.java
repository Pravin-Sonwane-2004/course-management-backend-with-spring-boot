package com.pravin.learnsphere_backend_with_spring_boot.controller;


import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;
import com.pravin.learnsphere_backend_with_spring_boot.service.CourseInstanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/instances")
public class CourseInstanceController {
    private final CourseInstanceService courseInstanceService;

    @Autowired
    public CourseInstanceController(CourseInstanceService courseInstanceService) {
        this.courseInstanceService = courseInstanceService;
    }

    @PostMapping
    public ResponseEntity<CourseInstance> createInstance(@RequestBody CourseInstance instance) {
        // TODO: Add validation and error handling
        CourseInstance created = courseInstanceService.createInstance(instance);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{year}/{semester}")
    public List<CourseInstance> getInstancesByYearAndSemester(@PathVariable int year, @PathVariable String semester) {
        return courseInstanceService.getInstancesByYearAndSemester(year, semester);
    }

    @GetMapping("/{year}/{semester}/{id}")
    public ResponseEntity<CourseInstance> getInstanceById(@PathVariable int year, @PathVariable String semester, @PathVariable Long id) {
        // Optionally, validate year/semester match
        return courseInstanceService.getInstanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{year}/{semester}/{id}")
    public ResponseEntity<Void> deleteInstance(@PathVariable int year, @PathVariable String semester, @PathVariable Long id) {
        // TODO: Add validation and error handling
        courseInstanceService.deleteInstance(id);
        return ResponseEntity.noContent().build();
    }

    }
