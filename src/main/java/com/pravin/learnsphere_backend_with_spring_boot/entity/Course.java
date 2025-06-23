package com.pravin.learnsphere_backend_with_spring_boot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_course_id", columnNames = "course_id"),
        @UniqueConstraint(name = "uk_course_name", columnNames = "name")
    }
)
@Getter
@Setter
@Schema(description = "Represents a course in the system")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the course", example = "1")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    @Schema(description = "Unique course identifier (e.g., CS101)", example = "CS101")
    private String courseId;

    @Column(nullable = false, length = 100)
    @Schema(description = "Course name", example = "Introduction to Computer Science")
    private String name;

    @Column(length = 1000)
    @Schema(description = "Course description", example = "Basic concepts of computer science")
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "course_prerequisites",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    @JsonIgnoreProperties({"prerequisites", "instances"})
    @Schema(description = "List of prerequisite courses")
    private Set<Course> prerequisites = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    @Schema(description = "List of course instances")
    private Set<CourseInstance> instances = new HashSet<>();

    public void addPrerequisite(Course prerequisite) {
        if (!prerequisites.contains(prerequisite)) {
            prerequisites.add(prerequisite);
            prerequisite.getPrerequisites().add(this);
        }
    }

    public void removePrerequisite(Course prerequisite) {
        prerequisites.remove(prerequisite);
        prerequisite.getPrerequisites().remove(this);
    }

    public void addInstance(CourseInstance instance) {
        instances.add(instance);
        instance.setCourse(this);
    }

    public void removeInstance(CourseInstance instance) {
        instances.remove(instance);
        instance.setCourse(null);
    }
}
