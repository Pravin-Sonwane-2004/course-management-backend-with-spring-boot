package com.pravin.learnsphere_backend_with_spring_boot.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

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

  @PostConstruct
  public void initializeTestCourses() {
    // Create test courses with prerequisites
    createTestCourse("CS101", "Introduction to Computer Science", "Basic concepts of computer science");
    createTestCourse("CS102", "Data Structures", "Fundamental data structures and algorithms");
    createTestCourse("CS201", "Algorithms", "Advanced algorithms and analysis", Set.of("CS102"));
    createTestCourse("CS202", "Operating Systems", "Computer operating systems", Set.of("CS101", "CS102"));
    createTestCourse("CS301", "Database Systems", "Database management systems");
    createTestCourse("CS302", "Computer Networks", "Computer networking concepts", Set.of("CS202"));
  }

  private void createTestCourse(String courseId, String name, String description, Set<String> prerequisites) {
    try {
      createCourse(courseId, name, description);
    } catch (Exception e) {
      System.out.println("Failed to create test course " + courseId + ": " + e.getMessage());
    }
  }

  private void createTestCourse(String courseId, String name, String description) {
    createTestCourse(courseId, name, description, null);
  }

  public List<Course> getAllCourses() {
    List<Course> courses = courseRepository.findAllWithDetails();
    // Process prerequisites to ensure they are properly fetched
    courses.forEach(course -> {
      if (course.getPrerequisites() != null) {
        course.getPrerequisites().forEach(prereq -> {
          // Ensure prerequisites are fully loaded
          prereq.getCourseId();
        });
      }
    });
    return courses;
  }

  public Optional<Course> getCourseById(Long id) {
    return courseRepository.findByIdWithPrerequisites(id);
  }

  public Optional<Course> getCourseByCourseId(String courseId) {
    return courseRepository.findByCourseIdWithPrerequisites(courseId);
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

    // Handle prerequisites if provided
    if (prerequisites != null && !prerequisites.isEmpty()) {
      for (String prereqId : prerequisites) {
        Course prereq = courseRepository.findByCourseId(prereqId)
            .orElseThrow(() -> new BadRequestException("Prerequisite course not found: " + prereqId));
        course.addPrerequisite(prereq);
      }
    }

    // Save the course first to get its ID
    Course savedCourse = courseRepository.save(course);

    // Prevent circular prerequisites
    if (hasCircularPrerequisite(savedCourse)) {
      throw new ConflictException("Circular prerequisite detected");
    }

    // If prerequisites are provided, save them after the course is saved
    if (prerequisites != null && !prerequisites.isEmpty()) {
      for (String prereqId : prerequisites) {
        Course prereq = courseRepository.findByCourseId(prereqId)
            .orElseThrow(() -> new BadRequestException("Prerequisite course not found: " + prereqId));
        savedCourse.addPrerequisite(prereq);
      }
      // Save the course again to persist the prerequisites
      return courseRepository.save(savedCourse);
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
          stack.push(prereq);
        }
      }
    }
    return false;
  }

  @Transactional
  public void deleteCourse(String courseId) {
    Course course = courseRepository.findByCourseId(courseId)
        .orElseThrow(() -> new NotFoundException("Course not found: courseId=" + courseId));

    // Check if course is used in any instances
    if (course.getInstances().size() > 0) {
      throw new BadRequestException("Cannot delete course as it is being used in course instances");
    }

    // Check if course is a prerequisite for any other courses
    List<Course> dependentCourses = courseRepository.findAll().stream()
        .filter(c -> c.getPrerequisites().contains(course))
        .collect(Collectors.toList());

    if (!dependentCourses.isEmpty()) {
      throw new BadRequestException("Cannot delete course as it is a prerequisite for other courses: " +
          dependentCourses.stream().map(Course::getName).collect(Collectors.joining(", ")));
    }

    courseRepository.delete(course);
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
}
