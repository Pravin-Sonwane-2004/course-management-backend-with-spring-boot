package com.pravin.learnsphere_backend_with_spring_boot.dto;

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
public class InstanceRequestDTO {
  private String courseId;
  private int year;
  private int semester;

  // Getters and Setters
}
