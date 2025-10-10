package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for managing Doctor-related operations including
 * CRUD and availability checking based on date and authentication token.
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * Get all doctors.
     */
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.findAll();
    }

    /**
     * Get doctor by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.findById(id);
        if (doctor != null) {
            return ResponseEntity.ok(doctor);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Create a new doctor.
     */
    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.save(doctor));
    }

    /**
     * ✅ Retrieve a doctor's availability based on role, doctorId, date, and token.
     */
    @GetMapping("/availability")
    public ResponseEntity<?> getDoctorAvailability(
            @RequestParam String role,
            @RequestParam Long doctorId,
            @RequestParam String date,
            @RequestHeader("Authorization") String token
    ) {
        // Simulate token validation
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing token.");
        }

        // Check role permission
        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Only ADMIN or STAFF can view availability.");
        }

        // Convert date string to LocalDate
        LocalDate targetDate;
        try {
            targetDate = LocalDate.parse(date);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use YYYY-MM-DD.");
        }

        // Get availability from service
        List<String> availableSlots = doctorService.getDoctorAvailability(doctorId, targetDate);
        if (availableSlots.isEmpty()) {
            return ResponseEntity.ok("No available slots for this date.");
        }

        return ResponseEntity.ok(availableSlots);
    }
}
