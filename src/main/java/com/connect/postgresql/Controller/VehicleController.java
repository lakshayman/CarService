package com.connect.postgresql.Controller;

import com.connect.postgresql.Entity.Vehicle;
import com.connect.postgresql.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping("/lookup")
    @Cacheable(value = "vehicles", key = "#regNumber")
    public Vehicle lookupVehicle(@RequestParam String regNumber) {
        System.out.println("Fetching from DB");
        return vehicleRepository.findByRegNumber(regNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
    }
}