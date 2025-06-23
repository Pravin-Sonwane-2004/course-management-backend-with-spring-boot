package com.pravin.learnsphere_backend_with_spring_boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;
import com.pravin.learnsphere_backend_with_spring_boot.exception.BadRequestException;
import com.pravin.learnsphere_backend_with_spring_boot.exception.NotFoundException;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseInstanceRepository;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseRepository;

import com.pravin.learnsphere_backend_with_spring_boot.dto.CourseInstanceDTO;
import java.util.stream.Collectors;

@Service
public class CourseInstanceService {
    private final CourseInstanceRepository courseInstanceRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseInstanceService(CourseInstanceRepository courseInstanceRepository, CourseRepository courseRepository) {
        this.courseInstanceRepository = courseInstanceRepository;
        this.courseRepository = courseRepository;
    }

    public List<CourseInstance> getInstancesByYearAndSemester(int year, int semester) {
        return courseInstanceRepository.findByYearAndSemester(year, semester);
    }

    public Optional<CourseInstance> getInstanceById(Long id) {
        return courseInstanceRepository.findById(id);
    }

    public Optional<CourseInstance> getInstanceByCourseIdAndYearAndSemester(String courseId, int year, int semester) {
        return courseInstanceRepository.findByCourseIdAndYearAndSemester(courseId, year, semester);
    }

    @Transactional
    public List<CourseInstanceDTO> getAllInstances() {
        return courseInstanceRepository.findAllWithDetails().stream()
            .map(instance -> {
                CourseInstanceDTO dto = new CourseInstanceDTO();
                dto.setInstanceId(instance.getInstanceId());
                dto.setCourseId(instance.getCourse().getCourseId());
                dto.setYear(instance.getYear());
                dto.setSemester(instance.getSemester());
                dto.setCourseName(instance.getCourse().getName());
                dto.setCourseDescription(instance.getCourse().getDescription());
                dto.setCoursePrerequisites(
                    instance.getCourse().getPrerequisites() != null
                        ? instance.getCourse().getPrerequisites().stream().map(p -> p.getCourseId()).toArray(String[]::new)
                        : new String[0]);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public CourseInstance createInstance(String courseId, int year, int semester) {
        // Validate course existence
        courseRepository.findByCourseId(courseId)
            .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

        // Validate year and semester
        if (year <= 0 || semester <= 0 || semester > 2) {
            throw new BadRequestException("Invalid year or semester value");
        }

        // Check if instance already exists
        Optional<CourseInstance> existingInstance = courseInstanceRepository.findByCourseIdAndYearAndSemester(courseId, year, semester);
        if (existingInstance.isPresent()) {
            throw new BadRequestException("Instance already exists for course " + courseId + " in year " + year + " semester " + semester);
        }

        // Create new instance
        CourseInstance instance = new CourseInstance();
        instance.setCourse(courseRepository.findByCourseId(courseId).get());
        instance.setYear(year);
        instance.setSemester(semester);
        instance.setInstanceId(courseId + "_" + year + "_" + semester);

        return courseInstanceRepository.save(instance);
    }

    @Transactional
    public void deleteInstance(Long id) {
        CourseInstance instance = courseInstanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course instance not found: id=" + id));
        
        // Remove instance from course's instances set
        instance.getCourse().getInstances().remove(instance);
        
        // Delete the instance
        courseInstanceRepository.deleteById(id);
    }

    @Transactional
    public void deleteInstanceByCourseIdAndYearAndSemester(String courseId, int year, int semester) {
        Optional<CourseInstance> instance = courseInstanceRepository.findByCourseIdAndYearAndSemester(courseId, year, semester);
        if (instance.isPresent()) {
            deleteInstance(instance.get().getId());
        }
    }
}