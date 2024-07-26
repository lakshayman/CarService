package com.connect.postgresql.Repository;

import com.connect.postgresql.Entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    List<Quotation> findByJobCardId(Long jobCardId);
}
