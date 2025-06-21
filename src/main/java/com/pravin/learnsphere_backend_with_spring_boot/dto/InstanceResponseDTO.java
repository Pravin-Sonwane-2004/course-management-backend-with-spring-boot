package com.pravin.learnsphere_backend_with_spring_boot.dto;

public record InstanceResponseDTO(
    String courseId,
    String title,
    int year,
    int semester) {
}