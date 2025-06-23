package com.pravin.learnsphere_backend_with_spring_boot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInstanceDTO {
    private String instanceId;
    private String courseId;
    private String courseName;
    private String courseDescription;
    private String[] coursePrerequisites;
    private Integer year;
    private Integer semester;
}

