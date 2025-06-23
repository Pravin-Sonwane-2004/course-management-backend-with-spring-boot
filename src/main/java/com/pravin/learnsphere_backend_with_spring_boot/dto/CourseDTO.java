package com.pravin.learnsphere_backend_with_spring_boot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private String courseId;
    private String name;
    private String description;
    private Set<String> prerequisites;
}

