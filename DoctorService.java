package com.yourorg.service;

import com.yourorg.model.Doctor;
import com.yourorg.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository repo;

    public List<Doctor> findAll() {
        return repo.findAll();
    }

    public Doctor findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Doctor save(Doctor d) {
        return repo.save(d);
    }
}
