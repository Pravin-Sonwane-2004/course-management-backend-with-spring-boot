package com.pravin.learnsphere_backend_with_spring_boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.learnsphere_backend_with_spring_boot.controller.CourseInstanceController;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.BadRequestException;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.NotFoundException;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseInstanceRepository;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseRepository;

@Service
public class CourseInstanceService {
    private final CourseInstanceRepository courseInstanceRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseInstanceService(CourseInstanceRepository courseInstanceRepository, CourseRepository courseRepository) {
        this.courseInstanceRepository = courseInstanceRepository;
        this.courseRepository = courseRepository;
    }

    public List<CourseInstance> getInstancesByYearAndSemester(int year, String semester) {
        return courseInstanceRepository.findByYearAndSemester(year, semester);
    }

    public Optional<CourseInstance> getInstanceById(Long id) {
        return courseInstanceRepository.findById(id);
    }

    @Transactional
    public CourseInstance createInstance(CourseInstance instance) {
        // Validate year and semester
        if (instance.getYear() <= 0 || instance.getSemester() == null || instance.getSemester().trim().isEmpty()) {
            throw new BadRequestException("Year and semester are required");
        }
        // Validate course exists
        if (instance.getCourse() == null || instance.getCourse().getId() == null) {
            throw new BadRequestException("Course ID is required for instance");
        }
        Course course = courseRepository.findById(instance.getCourse().getId())
                .orElseThrow(() -> new NotFoundException("Course not found: id=" + instance.getCourse().getId()));
        instance.setCourse(course);
        // Check for duplicate instance (same course, year, semester)
        boolean exists = courseInstanceRepository.findByYearAndSemester(instance.getYear(), instance.getSemester())
            .stream()
            .anyMatch(ci -> ci.getCourse().getId().equals(instance.getCourse().getId()));
        if (exists) {
            throw new BadRequestException("Course instance already exists for this course, year, and semester");
        }
        return courseInstanceRepository.save(instance);
    }

    @Transactional
    public void deleteInstance(Long id) {
        CourseInstance instance = courseInstanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course instance not found: id=" + id));
        // Add any dependency checks here if needed before deletion
        courseInstanceRepository.deleteById(id);
    }
}