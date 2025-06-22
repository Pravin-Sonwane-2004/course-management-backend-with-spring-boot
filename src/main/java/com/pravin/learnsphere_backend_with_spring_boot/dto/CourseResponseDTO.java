package com.pravin.learnsphere_backend_with_spring_boot.dto;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {
  private String courseId;
  private String name;
  private String description;
  private Set<String> prerequisites = new HashSet<>();

  public CourseResponseDTO(String courseId, String name, String description, List<String> prerequisites) {
    this.courseId = courseId;
    this.name = name;
    this.description = description;
    if (prerequisites != null) {
      this.prerequisites = new HashSet<>(prerequisites);
    }
  }
}
