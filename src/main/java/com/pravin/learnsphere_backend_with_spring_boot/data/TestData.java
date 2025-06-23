package com.pravin.learnsphere_backend_with_spring_boot.data;

import com.pravin.learnsphere_backend_with_spring_boot.entity.Course;
import com.pravin.learnsphere_backend_with_spring_boot.entity.CourseInstance;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseRepository;
import com.pravin.learnsphere_backend_with_spring_boot.repository.CourseInstanceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestData {
    @Bean
    CommandLineRunner initData(CourseRepository courseRepo, CourseInstanceRepository instanceRepo) {
        return args -> {
            // Only add CS101 if it doesn't exist
            if (!courseRepo.existsByCourseId("CS101")) {
                Course cs101 = new Course();
                cs101.setCourseId("CS101");
                cs101.setName("Intro to Computer Science");
                cs101.setDescription("Learn the basics of CS.");
                courseRepo.save(cs101);

                CourseInstance inst1 = new CourseInstance();
                inst1.setCourse(cs101);
                inst1.setYear(2024);
                inst1.setSemester(1);
                inst1.setInstanceId("CS101-2024-1");
                instanceRepo.save(inst1);
            }

            if (!courseRepo.existsByCourseId("MATH101")) {
                Course math101 = new Course();
                math101.setCourseId("MATH101");
                math101.setName("Calculus I");
                math101.setDescription("Introductory calculus.");
                courseRepo.save(math101);

                CourseInstance inst2 = new CourseInstance();
                inst2.setCourse(math101);
                inst2.setYear(2024);
                inst2.setSemester(2);
                inst2.setInstanceId("MATH101-2024-2");
                instanceRepo.save(inst2);
            }
        };
    }
}
