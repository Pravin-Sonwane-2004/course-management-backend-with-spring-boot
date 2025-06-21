package com.pravin.learnsphere_backend_with_spring_boot.dto;

public record InstanceRequestDTO(
    String courseId,
    int year,
    int semester) {
}
