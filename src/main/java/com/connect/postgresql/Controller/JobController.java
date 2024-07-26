package com.connect.postgresql.Controller;

import com.connect.postgresql.Entity.Job;
import com.connect.postgresql.Entity.JobCard;
import com.connect.postgresql.Entity.Technician;
import com.connect.postgresql.Repository.JobCardRepository;
import com.connect.postgresql.Repository.JobRepository;
import com.connect.postgresql.Repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobCardRepository jobCardRepository;
    @Autowired
    private TechnicianRepository technicianRepository;

    @PostMapping
    @CachePut(value = "jobs", key = "#result.id")
    public Job createJob(@RequestBody Job job) {
        JobCard jobCard = jobCardRepository.findById(job.getJobCard().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "JobCard not found"));
        Technician technician = technicianRepository.findById(job.getTechnician().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Technician not found"));

        job.setJobCard(jobCard);
        job.setTechnician(technician);
        job.setDescription(jobCard.getDescription());
        job.setStartDate(LocalDate.now());
        job.setStatus("ASSIGNED");
        return jobRepository.save(job);
    }

    @GetMapping
    @Cacheable(value = "jobs")
    public List<Job> getAllJobs() {
        System.out.println("Fetching from DB");
        return jobRepository.findAll();
    }

    @GetMapping("/{id}")
    @Cacheable(value = "jobs", key = "#id")
    public Job getJobById(@PathVariable Long id) {
        System.out.println("Fetching from DB");
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    }

    @PutMapping("/{id}")
    @CachePut(value = "jobs", key = "#id")
    public Job updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        if(jobDetails.getDescription() != null) {
            job.setDescription(jobDetails.getDescription());
        }

        if(jobDetails.getStatus() != null) {
            job.setStatus(jobDetails.getStatus());
        }

        if(jobDetails.getEndDate() != null) {
            job.setEndDate(jobDetails.getEndDate());
        }

        return jobRepository.save(job);
    }
}
