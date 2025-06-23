package com.pravin.learnsphere_backend_with_spring_boot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CourseRequestDTO {
  private String courseId;
  private String title;
  private String description;
  private List<String> prerequisites;
}