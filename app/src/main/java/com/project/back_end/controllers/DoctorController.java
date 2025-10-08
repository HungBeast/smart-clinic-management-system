package com.yourorg.controller;

import com.yourorg.model.Doctor;
import com.yourorg.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.findAll();
    }

    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable Long id) {
        return doctorService.findById(id);
    }

    @PostMapping
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.save(doctor);
    }
}
