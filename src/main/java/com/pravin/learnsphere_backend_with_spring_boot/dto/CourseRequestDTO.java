package com.pravin.learnsphere_backend_with_spring_boot.dto;

import java.util.List;

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
public class CourseRequestDTO {
  private String courseId;
  private String title;
  private String description;
  private List<String> prerequisites;

  // Getters and setters
}