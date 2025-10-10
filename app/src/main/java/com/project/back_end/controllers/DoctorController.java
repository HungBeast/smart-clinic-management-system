package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doc = doctorService.findById(id);
        if (doc == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(doc);
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor saved = doctorService.save(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Availability endpoint expects Authorization header and role param (as before)
    @GetMapping("/availability")
    public ResponseEntity<?> getAvailability(
            @RequestParam Long doctorId,
            @RequestParam String date,
            @RequestParam(required = false) String role,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        // Basic token check (you can replace with TokenService)
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Role check
        if (role != null && !(role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("STAFF"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        LocalDate d;
        try {
            d = LocalDate.parse(date);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("date must be YYYY-MM-DD");
        }

        List<String> slots = doctorService.getDoctorAvailability(doctorId, d);
        return ResponseEntity.ok(slots);
    }

    // Login validation endpoint (example)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        return doctorService.validateDoctorLogin(email, password)
                .map(doc -> ResponseEntity.ok(doc))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
    }
}
