package com.connect.postgresql.Controller;

import com.connect.postgresql.Entity.Customer;
import com.connect.postgresql.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/lookup")
    @Cacheable(value = "customers", key = "#contact")
    public Customer lookupCustomer(@RequestParam String contact) {
        System.out.println("Fetching from DB");
        return customerRepository.findByContact(contact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }
}
