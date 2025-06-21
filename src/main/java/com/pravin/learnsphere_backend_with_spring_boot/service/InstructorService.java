package com.pravin.learnsphere_backend_with_spring_boot.service;

import com.pravin.learnsphere_backend_with_spring_boot.dto.InstructorResponseDTO;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorService {
    public List<InstructorResponseDTO> getAllLecturers() {
        // Return mock data for testing
        List<InstructorResponseDTO> instructors = new ArrayList<>();
        instructors.add(new InstructorResponseDTO(1L, "Prof. Alice", "alice@univ.edu"));
        instructors.add(new InstructorResponseDTO(2L, "Prof. Bob", "bob@univ.edu"));
        instructors.add(new InstructorResponseDTO(3L, "Prof. Carol", "carol@univ.edu"));
        return instructors;
    }

    public InstructorResponseDTO createInstructor(InstructorResponseDTO dto) {
        // Stub: just return the DTO for now
        return dto;
    }
}
