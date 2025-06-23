package com.pravin.learnsphere_backend_with_spring_boot.dto;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDTO {
  private String courseId;
  private String name;
  private String description;
  private Set<String> prerequisites;
}

