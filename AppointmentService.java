package com.yourorg.service;

import com.yourorg.model.Appointment;
import com.yourorg.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository repo;

    public List<Appointment> findAllByPatient(Long patientId) {
        return repo.findByPatientId(patientId);
    }

    public Appointment save(Appointment appt) {
        return repo.save(appt);
    }
}
