package com.pravin.learnsphere_backend_with_spring_boot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String courseId;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "course_prerequisites",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    @JsonIgnoreProperties({"prerequisites", "instances"})
    private Set<Course> prerequisites = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
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
