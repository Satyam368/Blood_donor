package com.example.blood_backend.repository;

import com.example.blood_backend.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DonorRepository extends JpaRepository<Donor, Long> {
    @Query("SELECT d FROM Donor d WHERE (:city IS NULL OR LOWER(d.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND (:bloodType IS NULL OR UPPER(REPLACE(d.bloodType, ' ', '')) = UPPER(REPLACE(:bloodType, ' ', '')))")
    java.util.List<Donor> searchByCityAndBloodType(
            @Param("city") String city,
            @Param("bloodType") String bloodType
    );
}
