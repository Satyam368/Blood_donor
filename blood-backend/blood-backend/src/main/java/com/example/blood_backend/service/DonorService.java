package com.example.blood_backend.service;

import com.example.blood_backend.entity.Donor;
import com.example.blood_backend.repository.DonorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DonorService {
    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor saveDonor(Donor donor) {
        return donorRepository.save(donor);
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public List<Donor> findDonors(String city, String bloodType) {
        String cityParam = (city == null || city.isBlank()) ? null : city;
        String bloodTypeParam = (bloodType == null || bloodType.isBlank()) ? null : bloodType;
        return donorRepository.searchByCityAndBloodType(cityParam, bloodTypeParam);
    }

}
