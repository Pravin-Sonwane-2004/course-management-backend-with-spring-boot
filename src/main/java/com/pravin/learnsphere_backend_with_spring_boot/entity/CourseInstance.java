package com.pravin.learnsphere_backend_with_spring_boot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Entity
@Table(name = "course_instances")
@Getter
@Setter
public class CourseInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int semester;

    @Column(nullable = false, unique = true)
    private String instanceId;

    public CourseInstance() {}

    public CourseInstance(Course course, int year, int semester) {
        this.course = course;
        this.year = year;
        this.semester = semester;
        this.instanceId = String.format("%s-%d-%d", course.getCourseId(), year, semester);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseInstance that = (CourseInstance) o;
        return Objects.equals(instanceId, that.instanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId);
    }
}
