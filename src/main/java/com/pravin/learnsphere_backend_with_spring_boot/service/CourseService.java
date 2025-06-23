package com.pravin.learnsphere_backend_with_spring_boot.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;
import com.pravin.learnsphere_backend_with_spring_boot.exception.BadRequestException;
import com.pravin.learnsphere_backend_with_spring_boot.exception.ConflictException;
import com.pravin.learnsphere_backend_with_spring_boot.exception.NotFoundException;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseInstanceRepository;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseRepository;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseInstanceDTO;

@Service
@Slf4j
public class CourseService {
  private final CourseRepository courseRepository;
  private final CourseInstanceRepository courseInstanceRepository;

  public CourseService(CourseRepository courseRepository, CourseInstanceRepository courseInstanceRepository) {
    this.courseRepository = courseRepository;
    this.courseInstanceRepository = courseInstanceRepository;
  }

  @Transactional(readOnly = true)
  public List<Course> getAllCourses() {
    return courseRepository.findAllWithDetails();
  }

  public Optional<Course> getCourseById(Long id) {
    return courseRepository.findByIdWithPrerequisites(id);
  }

  public Optional<Course> getCourseByCourseId(String courseId) {
    return courseRepository.findByCourseIdWithPrerequisites(courseId);
  }

  @Transactional(readOnly = true)
  public List<CourseInstanceDTO> getAllInstances() {
    return courseInstanceRepository.findAllWithDetails().stream()
        .map(instance -> {
            CourseInstanceDTO dto = new CourseInstanceDTO();
            dto.setInstanceId(instance.getInstanceId());
            dto.setCourseId(instance.getCourse().getCourseId());
            dto.setYear(instance.getYear());
            dto.setSemester(instance.getSemester());
            return dto;
        })
        .collect(Collectors.toList());
  }

  @Transactional
  public Course createCourse(String courseId, String name, String description) {
    return createCourse(courseId, name, description, null);
  }

  @Transactional
  public Course createCourse(String courseId, String name, String description, Set<String> prerequisites) {
    // Validate inputs
    if (name == null || name.trim().isEmpty()) {
      throw new BadRequestException("Course name is required");
    }
    if (courseId == null || courseId.trim().isEmpty()) {
      throw new BadRequestException("Course ID is required");
    }

    // Check for duplicate courseId
    if (courseRepository.findByCourseId(courseId).isPresent()) {
      throw new ConflictException("Course with this ID already exists");
    }

    // Check for duplicate name
    if (courseRepository.findAll().stream()
        .anyMatch(c -> name.equals(c.getName()))) {
      throw new ConflictException("Course with this name already exists");
    }

    // Create new course
    Course course = new Course();
    course.setCourseId(courseId);
    course.setName(name);
    course.setDescription(description);

    // Save the course first to get its ID
    Course savedCourse = courseRepository.save(course);

    // Handle prerequisites if provided
    if (prerequisites != null && !prerequisites.isEmpty()) {
      // First ensure all prerequisites exist
      Set<Course> prereqCourses = new HashSet<>();
      for (String prereqId : prerequisites) {
        Course prereq = courseRepository.findByCourseIdWithPrerequisites(prereqId)
            .orElseThrow(() -> new BadRequestException("Prerequisite course not found: " + prereqId));
        prereqCourses.add(prereq);
      }

      // Check for circular prerequisites before adding
      if (hasCircularPrerequisite(savedCourse)) {
        throw new ConflictException("Circular prerequisite detected");
      }

      // Add prerequisites to the saved course
      for (Course prereq : prereqCourses) {
        savedCourse.addPrerequisite(prereq);
      }

      // Save all changes with prerequisites
      savedCourse = courseRepository.save(savedCourse);
    }

    return savedCourse;
  }

  private boolean hasCircularPrerequisite(Course course) {
    Set<String> visited = new HashSet<>();
    Deque<Course> stack = new ArrayDeque<>();
    stack.push(course);

    while (!stack.isEmpty()) {
      Course current = stack.pop();
      if (visited.add(current.getCourseId())) {
        for (Course prereq : current.getPrerequisites()) {
          if (prereq.getCourseId().equals(course.getCourseId())) {
            return true;
          }
          if (!visited.contains(prereq.getCourseId())) {
            stack.push(prereq);
          }
        }
      }
    }
    return false;
  }

  @Transactional
  public void deleteCourse(String courseId) {
    if (courseId == null || courseId.trim().isEmpty()) {
      throw new BadRequestException("Course ID cannot be null or empty");
    }

    log.info("Attempting to delete course with ID: {}", courseId);
    
    Course course = courseRepository.findByCourseIdWithPrerequisites(courseId)
        .orElseThrow(() -> new NotFoundException("Course not found with ID: " + courseId));

    // Check if course has active instances
    if (course.getInstances() != null && !course.getInstances().isEmpty()) {
      throw new ConflictException("Cannot delete course with active instances");
    }

    // Remove this course as a prerequisite from other courses
    courseRepository.findAll().forEach(other -> {
      if (other.getPrerequisites() != null) {
        other.getPrerequisites().remove(course);
      }
    });

    // Remove all prerequisites from this course
    if (course.getPrerequisites() != null) {
      course.getPrerequisites().forEach(prereq -> {
        course.removePrerequisite(prereq);
      });
    }

    // Delete the course
    courseRepository.delete(course);
    log.info("Successfully deleted course with ID: {}", courseId);
  }

  public void testCourseOperations() {
    try {
      // Test creating a course with prerequisites
      System.out.println("\nTesting course creation with prerequisites...");
      Course newCourse = createCourse("CS401", "Software Engineering", "Principles of software engineering");
      System.out.println("Created course: " + newCourse.getName());

      // Test circular prerequisite detection
      System.out.println("\nTesting circular prerequisites...");
      try {
        createCourse("CS402", "Advanced SE", "Advanced software engineering");
        System.out.println("Circular prerequisite test failed - should have thrown exception");
      } catch (ConflictException e) {
        System.out.println("Circular prerequisite test passed: " + e.getMessage());
      }

      // Test duplicate course creation
      System.out.println("\nTesting duplicate course...");
      try {
        createCourse("CS401", "Another Course", "Duplicate test");
        System.out.println("Duplicate course test failed - should have thrown exception");
      } catch (ConflictException e) {
        System.out.println("Duplicate course test passed: " + e.getMessage());
      }

      // Test course deletion with prerequisites
      System.out.println("\nTesting course deletion with prerequisites...");
      try {
        Optional<Course> cs101 = getCourseByCourseId("CS101");
        if (cs101.isPresent()) {
          deleteCourse("CS101");
          System.out.println("Course deletion test failed - should have thrown exception");
        }
      } catch (BadRequestException e) {
        System.out.println("Course deletion test passed: " + e.getMessage());
      }

      // Test successful course deletion
      System.out.println("\nTesting successful course deletion...");
      Optional<Course> cs401 = getCourseByCourseId("CS401");
      if (cs401.isPresent()) {
        deleteCourse("CS401");
        System.out.println("Successfully deleted course: CS401");
      }

      // List all courses
      System.out.println("\nAll courses:");
      getAllCourses()
          .forEach(course -> System.out.println(course.getCourseId() + " - " + course.getName() + " (Prerequisites: " +
              course.getPrerequisites().stream().map(c -> c.getCourseId()).collect(Collectors.joining(", ")) + ")"));

    } catch (Exception e) {
      System.err.println("Test failed: " + e.getMessage());
    }
  }

  @Transactional
  public CourseInstance createCourseInstance(CourseInstance instance) {
    if (instance.getCourse() == null) {
      throw new BadRequestException("Course must be specified for course instance");
    }
    
    if (instance.getYear() < 1900 || instance.getYear() > 2100) {
      throw new BadRequestException("Year must be between 1900 and 2100");
    }
    
    if (instance.getSemester() < 1 || instance.getSemester() > 2) {
      throw new BadRequestException("Semester must be 1 or 2");
    }
    
    // Check if instance already exists
    if (courseInstanceRepository.existsByCourseAndYearAndSemester(
        instance.getCourse(), instance.getYear(), instance.getSemester())) {
      throw new ConflictException("Course instance already exists for this course, year, and semester");
    }
    
    // Generate instance ID if not provided
    if (instance.getInstanceId() == null) {
      instance.setInstanceId(String.format("%s-%d-%d", 
          instance.getCourse().getCourseId(), instance.getYear(), instance.getSemester()));
    }
    
    return courseInstanceRepository.save(instance);
  }
}
