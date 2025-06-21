package com.pravin.learnsphere_backend_with_spring_boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.pravin.learnsphere_backend_with_spring_boot.dto.InstructorResponseDTO;
import com.pravin.learnsphere_backend_with_spring_boot.service.InstructorService;

@RestController
@RequestMapping("/api")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    // Get all lecturers (legacy endpoint for frontend compatibility)
    @GetMapping("/get-all-lecturers")
    public ResponseEntity<List<InstructorResponseDTO>> getAllLecturers() {
        return ResponseEntity.ok(instructorService.getAllLecturers());
    }

    // Create instructor
    @PostMapping("/lecturer/save")
    public ResponseEntity<InstructorResponseDTO> createInstructor(@RequestBody InstructorResponseDTO dto) {
        InstructorResponseDTO response = instructorService.createInstructor(dto);
        return ResponseEntity.status(201).body(response);
    }
}
