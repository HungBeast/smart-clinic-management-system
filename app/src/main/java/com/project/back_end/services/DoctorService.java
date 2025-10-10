package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.repositories.DoctorRepository;
import com.project.back_end.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // ✅ CRUD mặc định
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // ✅ 1. Xác thực đăng nhập (login validation)
    public Doctor validateDoctorLogin(String email, String password) {
        return doctorRepository.findByEmailAndPassword(email, password)
                .orElse(null);
    }

    // ✅ 2. Lấy danh sách giờ rảnh dựa trên Appointment có sẵn
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {

        List<LocalDateTime> booked = appointmentRepository
                .findByDoctorIdAndAppointmentDate(doctorId, date);

        // Giả lập ca sáng từ 8h đến 17h (tuỳ bạn chỉnh nếu muốn)
        List<String> availableSlots = new ArrayList<>();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(17, 0);

        for (LocalTime time = start; time.isBefore(end); time = time.plusHours(1)) {
            LocalDateTime slot = LocalDateTime.of(date, time);
            boolean isBooked = booked.stream()
                    .anyMatch(appt -> appt.getHour() == slot.getHour());
            if (!isBooked) {
                availableSlots.add(time.toString());
            }
        }

        return availableSlots;
    }
}
