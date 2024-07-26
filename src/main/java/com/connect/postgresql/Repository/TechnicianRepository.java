package com.connect.postgresql.Repository;

import com.connect.postgresql.Entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {
}
