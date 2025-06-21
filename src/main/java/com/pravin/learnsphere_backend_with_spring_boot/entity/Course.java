package com.pravin.learnsphere_backend_with_spring_boot.entity;

import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.JoinColumn;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String courseId;

  private String title;
  private String description;

  @ManyToMany
  @JoinTable(name = "course_prerequisites", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "prerequisite_id"))
  private List<Course> prerequisites;
}
