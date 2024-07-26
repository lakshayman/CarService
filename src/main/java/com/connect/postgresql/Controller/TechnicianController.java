package com.connect.postgresql.Controller;

import com.connect.postgresql.Entity.Technician;
import com.connect.postgresql.Repository.TechnicianRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {

    @Autowired
    private TechnicianRepository technicianRepository;

    @PostMapping
    @CachePut(value = "technicians", key = "#result.id")
    public Technician createTechnician(@Valid @RequestBody Technician technician) {
        return technicianRepository.save(technician);
    }

    @GetMapping
    @Cacheable(value = "technicians")
    public List<Technician> getAllTechnicians() {
        System.out.println("Fetching from DB");
        return technicianRepository.findAll();
    }

    @GetMapping("/{id}")
    @Cacheable(value = "technicians", key = "#id")
    public Technician getTechnicianById(@PathVariable Long id) {
        System.out.println("Fetching from DB");
        return technicianRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Technician not found"));
    }

    @PutMapping("/{id}")
    @CachePut(value = "technicians", key = "#id")
    public Technician updateTechnician(@PathVariable Long id, @RequestBody Technician technicianDetails) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Technician not found"));

        if(technicianDetails.getName() != null) {
            technician.setName(technicianDetails.getName());
        }

        if(technicianDetails.getContact() != null) {
            technician.setContact(technicianDetails.getContact());
        }

        return technicianRepository.save(technician);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "technicians", key = "#id")
    public ResponseEntity<?> deleteTechnician(@PathVariable Long id) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Technician not found"));

        technicianRepository.delete(technician);
        return ResponseEntity.ok().build();
    }
}
