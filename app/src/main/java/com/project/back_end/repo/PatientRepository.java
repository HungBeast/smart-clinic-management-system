package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * ✅ Derived query method to find a patient by email.
     * Example usage: patientRepository.findByEmail("abc@gmail.com");
     */
    Patient findByEmail(String email);

    /**
     * ✅ Custom JPQL query to find patient by email or phone number.
     * Example usage: patientRepository.findByEmailOrPhone("abc@gmail.com", "0123456789");
     */
    @Query("SELECT p FROM Patient p WHERE p.email = :email OR p.phone = :phone")
    Patient findByEmailOrPhone(@Param("email") String email, @Param("phone") String phone);
}
