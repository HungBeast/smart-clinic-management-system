package com.yourorg.controller;

import com.yourorg.model.Prescription;
import com.yourorg.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping
    public Prescription create(@RequestBody Prescription p) {
        return prescriptionService.save(p);
    }
}
