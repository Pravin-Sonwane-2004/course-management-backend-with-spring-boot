package com.pravin.learnsphere_backend_with_spring_boot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pravin.learnsphere_backend_with_spring_boot.dto.InstanceRequestDTO;
import com.pravin.learnsphere_backend_with_spring_boot.dto.InstanceResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;
import com.pravin.learnsphere_backend_with_spring_boot.exceptions.NotFoundException;
import com.pravin.learnsphere_backend_with_spring_boot.mapper.CourseInstanceMapper;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseInstanceRepository;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseRepository;

@Service
public class CourseInstanceService {

  @Autowired
  private CourseInstanceRepository instanceRepo;

  @Autowired
  private CourseRepository courseRepo;

  public InstanceResponseDTO createInstance(InstanceRequestDTO dto) {
    Course course = courseRepo.findByCourseId(dto.getCourseId())
        .orElseThrow(() -> new NotFoundException("Course not found"));

    CourseInstance instance = CourseInstanceMapper.toEntity(dto, course);
    instanceRepo.save(instance);

    return CourseInstanceMapper.toDTO(instance);
  }

  public List<InstanceResponseDTO> listInstances(int year, int semester) {
    return instanceRepo.findByYearAndSemester(year, semester).stream()
        .map(CourseInstanceMapper::toDTO)
        .collect(Collectors.toList());
  }

  public InstanceResponseDTO getInstance(int year, int semester, String courseId) {
    CourseInstance instance = instanceRepo.findByYearAndSemesterAndCourse_CourseId(year, semester, courseId)
        .orElseThrow(() -> new NotFoundException("Instance not found"));
    return CourseInstanceMapper.toDTO(instance);
  }

  public void deleteInstance(int year, int semester, String courseId) {
    CourseInstance instance = instanceRepo.findByYearAndSemesterAndCourse_CourseId(year, semester, courseId)
        .orElseThrow(() -> new NotFoundException("Instance not found"));

    instanceRepo.delete(instance);
  }
}
