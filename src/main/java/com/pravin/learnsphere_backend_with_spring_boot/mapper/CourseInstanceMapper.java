package com.pravin.learnsphere_backend_with_spring_boot.mapper;

import com.pravin.learnsphere_backend_with_spring_boot.dto.InstanceRequestDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.InstanceResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;

public class CourseInstanceMapper {

  public static InstanceResponseDTO toDTO(CourseInstance instance) {
    return new InstanceResponseDTO(
        instance.getCourse().getCourseId(),
        instance.getCourse().getTitle(),
        instance.getYear(),
        instance.getSemester());
  }

  public static CourseInstance toEntity(InstanceRequestDTO dto, Course course) {
    CourseInstance instance = new CourseInstance();
    instance.setCourse(course);
    instance.setYear(dto.getYear());
    instance.setSemester(dto.getSemester());
    return instance;
  }
}
