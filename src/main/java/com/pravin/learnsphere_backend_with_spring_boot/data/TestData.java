package com.pravin.learnsphere_backend_with_spring_boot.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseInstanceDTO;

public class TestData {

  public static List<CourseDTO> getCourses() {
    List<CourseDTO> courses = new ArrayList<>();

    // Core Computer Science Courses
    courses.add(
        new CourseDTO("CS101", "Introduction to Computer Science", "Basic concepts of computer science", Set.of()));
    courses.add(new CourseDTO("CS102", "Data Structures", "Fundamental data structures and algorithms", Set.of()));
    courses.add(new CourseDTO("CS201", "Algorithms", "Advanced algorithms and analysis", Set.of("CS102")));
    courses.add(new CourseDTO("CS202", "Operating Systems", "Computer operating systems", Set.of("CS101", "CS102")));
    courses.add(new CourseDTO("CS301", "Database Systems", "Database management systems", Set.of()));
    courses.add(new CourseDTO("CS302", "Computer Networks", "Computer networking concepts", Set.of("CS202")));

    // Software Engineering Courses
    courses.add(new CourseDTO("SE201", "Software Engineering Fundamentals",
        "Introduction to software engineering principles", Set.of("CS101", "CS102")));
    courses.add(new CourseDTO("SE301", "Advanced Software Engineering", "Advanced topics in software engineering",
        Set.of("SE201", "CS201")));

    // Mathematics Courses
    courses.add(new CourseDTO("MATH101", "Calculus I", "Introduction to calculus", Set.of()));
    courses.add(new CourseDTO("MATH102", "Calculus II", "Advanced calculus topics", Set.of("MATH101")));

    // Special Topics
    courses.add(new CourseDTO("AI101", "Introduction to Artificial Intelligence", "Basic concepts of AI",
        Set.of("CS101", "MATH101")));
    courses.add(new CourseDTO("AI201", "Machine Learning", "Introduction to machine learning algorithms",
        Set.of("AI101", "MATH102")));

    return courses;
  }

  public static CourseDTO getSampleCourse() {
    return new CourseDTO("CS101", "Introduction to Computer Science", "Basic concepts of computer science", Set.of());
  }

  public static CourseInstanceDTO getSampleCourseInstance() {
    return new CourseInstanceDTO();
  }

  public static List<CourseDTO> getSampleCoursesForFrontend() {
    List<CourseDTO> courses = new ArrayList<>();

    courses.add(
        new CourseDTO("CS101", "Introduction to Computer Science", "Basic concepts of computer science", Set.of()));
    courses.add(new CourseDTO("CS102", "Data Structures", "Fundamental data structures and algorithms", Set.of()));
    courses.add(new CourseDTO("CS201", "Algorithms", "Advanced algorithms and analysis", Set.of("CS102")));

    return courses;
  }

  public static List<CourseInstanceDTO> getSampleCourseInstancesForFrontend() {
    List<CourseInstanceDTO> instances = new ArrayList<>();

    instances.add(new CourseInstanceDTO());
    instances.add(new CourseInstanceDTO());

    return instances;
  }
}
