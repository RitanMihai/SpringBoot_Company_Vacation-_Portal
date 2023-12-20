package com.lab.model.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * JPA Entities
 */
@Entity
@Table(name="days_off")
public class DaysOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(name="is_approved")
    private Boolean isApproved;

    @Column(name="message")
    private String message;

    @ManyToOne
    private UserEntity user;
}
