package com.connect.postgresql.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job")
public class Job implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "job_card_id")
    private JobCard jobCard;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

}
