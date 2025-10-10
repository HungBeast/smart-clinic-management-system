package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repositories.AppointmentRepository;
import com.project.back_end.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Basic CRUD
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Validate doctor credentials (email + password)
    public Optional<Doctor> validateDoctorLogin(String email, String password) {
        return doctorRepository.findByEmailAndPassword(email, password);
    }

    /**
     * Return available time slots for a doctor on a given date.
     * This implementation:
     *  - defines working hours (08:00 - 17:00)
     *  - uses 30-minute slots (adjust as needed)
     *  - excludes slots where an appointment exists in that hour/minute
     */
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        // Fetch appointments for the doctor on the date
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
        Set<LocalDateTime> booked = appointments.stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toSet());

        List<String> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(17, 0);

        LocalDateTime cursor = LocalDateTime.of(date, start);
        LocalDateTime endDateTime = LocalDateTime.of(date, end);

        while (!cursor.isAfter(endDateTime.minusMinutes(30))) {
            // slot range cursor .. cursor+30min
            boolean occupied = booked.stream().anyMatch(b -> b.equals(cursor));
            if (!occupied) {
                slots.add(cursor.toString()); // e.g. 2025-10-08T09:00
            }
            cursor = cursor.plusMinutes(30);
        }
        return slots;
    }
}
