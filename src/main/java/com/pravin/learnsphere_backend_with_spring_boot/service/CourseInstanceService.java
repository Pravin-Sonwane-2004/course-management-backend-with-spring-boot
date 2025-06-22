package com.pravin.learnsphere_backend_with_spring_boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<CourseInstance> getInstancesByYearAndSemester(int year, int semester) {
        return courseInstanceRepository.findByYearAndSemester(year, semester);
    }

    public Optional<CourseInstance> getInstanceById(Long id) {
        return courseInstanceRepository.findById(id);
    }

    public Optional<CourseInstance> getInstanceByCourseIdAndYearAndSemester(String courseId, int year, int semester) {
        return courseInstanceRepository.findByCourseIdAndYearAndSemester(courseId, year, semester);
    }

    public List<CourseInstance> getAllInstances() {
        return courseInstanceRepository.findAll();
    }

    @Transactional
    public CourseInstance createInstance(String courseId, int year, int semester) {
        // Validate year and semester
        if (year <= 0 || semester <= 0 || semester > 2) {
            throw new BadRequestException("Invalid year or semester value");
        }
        // Validate course exists
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));
        
        // Check for duplicate instance
        if (courseInstanceRepository.findByCourseIdAndYearAndSemester(courseId, year, semester).isPresent()) {
            throw new BadRequestException("Course instance already exists for this course, year, and semester");
        }
        
        // Create new instance
        CourseInstance instance = new CourseInstance(course, year, semester);
        return courseInstanceRepository.save(instance);
    }

    @Transactional
    public void deleteInstance(Long id) {
        CourseInstance instance = courseInstanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course instance not found: id=" + id));
        
        // Remove instance from course's instances set
        instance.getCourse().getInstances().remove(instance);
        
        // Delete the instance
        courseInstanceRepository.deleteById(id);
    }

    @Transactional
    public void deleteInstanceByCourseIdAndYearAndSemester(String courseId, int year, int semester) {
        Optional<CourseInstance> instance = courseInstanceRepository.findByCourseIdAndYearAndSemester(courseId, year, semester);
        if (instance.isPresent()) {
            deleteInstance(instance.get().getId());
        }
    }
}