package com.pravin.learnsphere_backend_with_spring_boot.dto;

import java.util.List;

public record CourseRequestDTO(
    String courseId,
    String title,
    String description,
    
  List <String> prerequisites) {
}

