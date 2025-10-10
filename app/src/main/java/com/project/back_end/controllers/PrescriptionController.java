package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling Prescription operations.
 */
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    /**
     * ✅ Create a new prescription (with token validation and input validation)
     * @param token authentication token passed in the path
     * @param prescription validated prescription data
     * @return structured JSON response
     */
    @PostMapping("/create/{token}")
    public ResponseEntity<?> createPrescription(
            @PathVariable("token") String token,
            @Valid @RequestBody Prescription prescription) {

        // (Optional) Validate token before saving
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or missing token");
        }

        try {
            Prescription savedPrescription = prescriptionService.savePrescription(prescription);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPrescription);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create prescription: " + e.getMessage());
        }
    }
}
