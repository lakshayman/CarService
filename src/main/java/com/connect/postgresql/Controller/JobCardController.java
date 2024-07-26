package com.connect.postgresql.Controller;

import com.connect.postgresql.Entity.Customer;
import com.connect.postgresql.Entity.JobCard;
import com.connect.postgresql.Entity.Vehicle;
import com.connect.postgresql.Repository.CustomerRepository;
import com.connect.postgresql.Repository.JobCardRepository;
import com.connect.postgresql.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/jobcards")
public class JobCardController {

    @Autowired
    private JobCardRepository jobCardRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    @CachePut(value = "jobCards", key = "#result.id")
    public JobCard createJobCard(@RequestBody JobCard jobCardRequest) {
        JobCard jobCard = new JobCard();
        jobCard.setDescription(jobCardRequest.getDescription());
        jobCard.setDate(LocalDate.now());
        jobCard.setStatus("OPEN");

        Vehicle vehicle;
        if (jobCardRequest.getVehicle().getId() != null) {
            // Existing vehicle
            vehicle = vehicleRepository.findById(jobCardRequest.getVehicle().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
        } else {
            // New vehicle
            vehicle = new Vehicle();
            vehicle.setRegNumber(jobCardRequest.getVehicle().getRegNumber());
            vehicle.setMake(jobCardRequest.getVehicle().getMake());
            vehicle.setModel(jobCardRequest.getVehicle().getModel());
            vehicle.setYear(jobCardRequest.getVehicle().getYear());

            if (jobCardRequest.getVehicle().getCustomer().getId() != null) {
                // Existing customer
                Customer customer = customerRepository.findById(jobCardRequest.getVehicle().getCustomer().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
                vehicle.setCustomer(customer);
            } else {
                // New customer
                Customer customer = new Customer();
                customer.setName(jobCardRequest.getVehicle().getCustomer().getName());
                customer.setContact(jobCardRequest.getVehicle().getCustomer().getContact());
                customer.setAddress(jobCardRequest.getVehicle().getCustomer().getAddress());
                customer = customerRepository.save(customer);
                vehicle.setCustomer(customer);
            }

            vehicle = vehicleRepository.save(vehicle);
        }
        jobCard.setVehicle(vehicle);

        return jobCardRepository.save(jobCard);
    }

    @GetMapping
    @Cacheable(value = "jobCards")
    public List<JobCard> getAllJobCards() {
        System.out.println("Fetching from DB");
        return jobCardRepository.findAll();
    }

    @GetMapping("/{id}")
    @Cacheable(value = "jobCards", key = "#id")
    public JobCard getJobCardById(@PathVariable Long id) {
        JobCard jobCard = jobCardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "JobCard not found"));
        System.out.println("Fetching from DB " + jobCard.getId());
        return jobCard;
    }

    @PutMapping("/{id}")
    @CachePut(value = "jobCards", key = "#id")
    public JobCard updateJobCard(@PathVariable Long id, @RequestBody JobCard jobCardReq) {
        JobCard jobCard = jobCardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "JobCard not found"));

        if(jobCardReq.getDescription() != null){
            jobCard.setDescription(jobCardReq.getDescription());
        }
        if(jobCardReq.getStatus() != null){
            jobCard.setStatus(jobCardReq.getStatus());
        }

        return jobCardRepository.save(jobCard);
    }

}
