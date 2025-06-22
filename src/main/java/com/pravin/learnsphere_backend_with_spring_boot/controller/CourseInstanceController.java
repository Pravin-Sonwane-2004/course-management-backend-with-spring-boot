package com.pravin.learnsphere_backend_with_spring_boot.controller;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseInstanceDTO;
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
    public ResponseEntity<CourseInstance> createInstance(@RequestBody CourseInstanceDTO dto) {
        CourseInstance created = courseInstanceService.createInstance(
            dto.getCourseId(), 
            dto.getYear(), 
            dto.getSemester()
        );
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{year}/{semester}")
    public CourseInstanceDTO[] getInstancesByYearAndSemester(@PathVariable int year, @PathVariable int semester) {
        List<CourseInstance> instances = courseInstanceService.getInstancesByYearAndSemester(year, semester);
        CourseInstanceDTO[] dtos = new CourseInstanceDTO[instances.size()];
        int index = 0;
        for (CourseInstance instance : instances) {
            CourseInstanceDTO dto = new CourseInstanceDTO();
            dto.setInstanceId(instance.getInstanceId());
            dto.setCourseId(instance.getCourse().getCourseId());
            dto.setYear(instance.getYear());
            dto.setSemester(instance.getSemester());
            dtos[index++] = dto;
        }
        return dtos;
    }

    @GetMapping
    public List<CourseInstance> getAllInstances() {
        return courseInstanceService.getAllInstances();
    }

    @GetMapping("/{year}/{semester}/{courseId}")
    public ResponseEntity<CourseInstance> getInstanceByCourseId(@PathVariable int year, @PathVariable int semester, @PathVariable String courseId) {
        return courseInstanceService.getInstanceByCourseIdAndYearAndSemester(courseId, year, semester)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{year}/{semester}/{courseId}")
    public ResponseEntity<Void> deleteInstance(@PathVariable int year, @PathVariable int semester, @PathVariable String courseId) {
        courseInstanceService.deleteInstanceByCourseIdAndYearAndSemester(courseId, year, semester);
        return ResponseEntity.noContent().build();
    }
}
