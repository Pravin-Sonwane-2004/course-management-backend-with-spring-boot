package com.pravin.learnsphere_backend_with_spring_boot.service;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.exception.ConflictException;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.BadRequestException;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.NotFoundException;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseInstanceRepository;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseRepository;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseInstanceRepository courseInstanceRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseInstanceRepository courseInstanceRepository) {
        this.courseRepository = courseRepository;
        this.courseInstanceRepository = courseInstanceRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public Course createCourse(Course course) {
        // Validate name
        if (course.getName() == null || course.getName().trim().isEmpty()) {
            throw new BadRequestException("Course name is required");
        }
        // Check for duplicate name
        if (courseRepository.findByName(course.getName()).isPresent()) {
            throw new ConflictException("Course with this name already exists");
        }
        // Validate prerequisites
        Set<Course> validPrereqs = new HashSet<>();
        if (course.getPrerequisites() != null) {
            for (Course prereq : course.getPrerequisites()) {
                Course found = courseRepository.findById(prereq.getId())
                        .orElseThrow(() -> new BadRequestException("Prerequisite course not found: id=" + prereq.getId()));
                validPrereqs.add(found);
            }
        }
        // Prevent self-reference
        if (validPrereqs.stream().anyMatch(pr -> pr.getName().equals(course.getName()))) {
            throw new ConflictException("A course cannot be its own prerequisite");
        }
        // Prevent circular prerequisites
        if (hasCircularPrerequisite(course.getName(), validPrereqs)) {
            throw new ConflictException("Circular prerequisite detected");
        }
        course.setPrerequisites(validPrereqs);
        return courseRepository.save(course);
    }

    private boolean hasCircularPrerequisite(String courseName, Set<Course> prereqs) {
        Set<String> visited = new HashSet<>();
        Deque<Course> stack = new ArrayDeque<>(prereqs);
        while (!stack.isEmpty()) {
            Course current = stack.pop();
            if (current.getName().equals(courseName)) {
                return true;
            }
            if (visited.add(current.getName()) && current.getPrerequisites() != null) {
                stack.addAll(current.getPrerequisites());
            }
        }
        return false;
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: id=" + id));
        // Check if this course is a prerequisite for any other course
        List<Course> allCourses = courseRepository.findAll();
        for (Course c : allCourses) {
            if (c.getPrerequisites() != null && c.getPrerequisites().stream().anyMatch(pr -> pr.getId().equals(id))) {
                throw new ConflictException("Cannot delete course: it is a prerequisite for another course");
            }
        }
        courseRepository.deleteById(id);
    }
}