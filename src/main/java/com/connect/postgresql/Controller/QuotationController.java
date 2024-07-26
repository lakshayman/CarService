package com.connect.postgresql.Controller;

import com.connect.postgresql.Entity.JobCard;
import com.connect.postgresql.Entity.Quotation;
import com.connect.postgresql.Repository.JobCardRepository;
import com.connect.postgresql.Repository.QuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private JobCardRepository jobCardRepository;

    @PostMapping
    @CachePut(value = "quotations", key = "#result.id")
    public Quotation createQuotation(@RequestBody Quotation quotation) {
        JobCard jobCard = jobCardRepository.findById(quotation.getJobCard().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "JobCard not found"));

        quotation.setJobCard(jobCard);
        quotation.setDescription(jobCard.getDescription());
        quotation.setStatus("PENDING");
        quotation.setDate(LocalDate.now());

        return quotationRepository.save(quotation);
    }

    @GetMapping
    @Cacheable(value = "quotations")
    public List<Quotation> getAllQuotations() {
        System.out.println("Fetching from DB");
        return quotationRepository.findAll();
    }

    @GetMapping("/{id}")
    @Cacheable(value = "quotations", key = "#id")
    public Quotation getQuotationById(@PathVariable Long id) {
        System.out.println("Fetching from DB");
        return quotationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quotation not found"));
    }

    @GetMapping("/jobcard/{jobCardId}")
    @Cacheable(value = "quotations", key = "'jobCardId-' + #jobCardId")
    public List<Quotation> getQuotationsByJobCardId(@PathVariable Long jobCardId) {
        System.out.println("Fetching from DB");
        return quotationRepository.findByJobCardId(jobCardId);
    }

    @PutMapping("/{id}")
    @CachePut(value = "quotations", key = "#id")
    public Quotation updateQuotation(@PathVariable Long id, @RequestBody Quotation quotationDetails) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quotation not found"));

        if(quotationDetails.getAmount() != null) {
            quotation.setAmount(quotationDetails.getAmount());
        }

        if(quotationDetails.getDescription() != null) {
            quotation.setDescription(quotationDetails.getDescription());
        }

        if(quotationDetails.getStatus() != null) {
            quotation.setStatus(quotationDetails.getStatus());
        }

        return quotationRepository.save(quotation);
    }

}
