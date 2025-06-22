package com.pravin.learnsphere_backend_with_spring_boot.dto;

import java.util.Set;
import java.util.HashSet;

public class CourseDTO {
    private String courseId;
    private String name;
    private String description;
    private Set<String> prerequisites = new HashSet<>();

    public CourseDTO() {}

    public CourseDTO(String courseId, String name, String description, Set<String> prerequisites) {
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        if (prerequisites != null) {
            this.prerequisites = new HashSet<>(prerequisites);
        }
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(Set<String> prerequisites) {
        if (prerequisites != null) {
            this.prerequisites = new HashSet<>(prerequisites);
        }
    }
}
