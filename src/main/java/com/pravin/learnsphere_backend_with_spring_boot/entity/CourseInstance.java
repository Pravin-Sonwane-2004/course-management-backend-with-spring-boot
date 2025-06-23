package com.pravin.learnsphere_backend_with_spring_boot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Entity
@Table(name = "course_instances",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_instance_id", columnNames = "instance_id"),
        @UniqueConstraint(name = "uk_course_year_semester", columnNames = {"course_id", "year", "semester"})
    }
)
@Getter
@Setter
@Schema(description = "Represents a specific instance of a course in a particular year and semester")
public class CourseInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the course instance", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(name = "fk_instance_course"))
    @JsonBackReference
    @Schema(description = "The course this instance belongs to")
    private Course course;

    @Column(nullable = false)
    @Schema(description = "Academic year of the course instance", example = "2025")
    private int year;

    @Column(nullable = false)
    @Schema(description = "Semester number (1 or 2)", example = "1")
    private int semester;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "Unique identifier for the course instance (e.g., CS101-2025-1)", example = "CS101-2025-1")
    private String instanceId;

    public CourseInstance() {}

    public CourseInstance(Course course, int year, int semester) {
        if (year < 1900 || year > 2100) {
            throw new IllegalArgumentException("Year must be between 1900 and 2100");
        }
        if (semester < 1 || semester > 2) {
            throw new IllegalArgumentException("Semester must be 1 or 2");
        }
        
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
