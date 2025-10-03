package com.example.blood_backend.controller;

import com.example.blood_backend.entity.Donor;
import jakarta.validation.Valid;
import com.example.blood_backend.service.DonorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = {"http://localhost:8083", "http://localhost:8080", "http://localhost:3001", "http://localhost:5173"}) // allow React frontend on multiple ports
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping
    public Donor registerDonor(@Valid @RequestBody Donor donor) {
        return donorService.saveDonor(donor);
    }

    @GetMapping
    public List<Donor> getDonors() {
        return donorService.getAllDonors();
    }
}
