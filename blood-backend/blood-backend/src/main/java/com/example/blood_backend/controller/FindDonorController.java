package com.example.blood_backend.controller;

import com.example.blood_backend.entity.Donor;
import com.example.blood_backend.service.DonorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/find-donors")
@CrossOrigin(origins = "http://localhost:5173")
public class FindDonorController {

    private final DonorService donorService;

    public FindDonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @GetMapping("/search")
    public List<Donor> search(@RequestParam(required = false) String city,
                              @RequestParam(required = false) String bloodType) {
        return donorService.findDonors(city, bloodType);
    }
}


